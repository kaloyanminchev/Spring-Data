package com.jsonexdemo.services.impls;

import com.jsonexdemo.models.dtos.queryDtos.query2.SoldProductDto;
import com.jsonexdemo.models.dtos.queryDtos.query2.UserWithSuccessfullySoldProductsDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.ProductsViewDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.SoldProductsViewDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.UserViewDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.UsersAndProductsViewDto;
import com.jsonexdemo.models.dtos.seedDtos.UserSeedDto;
import com.jsonexdemo.models.entities.User;
import com.jsonexdemo.repositories.UserRepository;
import com.jsonexdemo.services.UserService;
import com.jsonexdemo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers(UserSeedDto[] userSeedDtos) {
        Arrays.stream(userSeedDtos)
                .forEach(userSeedDto -> {

                    if (this.validationUtil.isValid(userSeedDto)) {
                        if (this.userRepository
                                .findByFirstNameAndLastName(userSeedDto.getFirstName(),
                                        userSeedDto.getLastName()) == null) {
                            User user = this.modelMapper.map(userSeedDto, User.class);
                            this.userRepository.saveAndFlush(user);
                        } else {
                            System.out.printf("%s already exists in database!",
                                    userSeedDto.getLastName());
                        }
                    } else {
                        this.validationUtil
                                .violations(userSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public User getRandomUser() {
        Random random = new Random();
        long randomId = random.nextInt((int) this.userRepository.count()) + 1;

        return this.userRepository.getOne(randomId);
    }

    @Override
    public List<UserWithSuccessfullySoldProductsDto> getUsersByProducts(int number) {
        List<UserWithSuccessfullySoldProductsDto> users = this.userRepository
                .findAllBySoldProductsSizeGreaterThan(number)
                .stream()
                .map(user -> {

                    UserWithSuccessfullySoldProductsDto userProductsDto =
                            this.modelMapper.map(user, UserWithSuccessfullySoldProductsDto.class);

                    List<SoldProductDto> soldProducts = new ArrayList<>();
                    user.getSoldProducts()
                            .forEach(product -> {

                                if (product.getBuyer() != null) {
                                    SoldProductDto soldProductsDto =
                                            this.modelMapper.map(product, SoldProductDto.class);
                                    soldProductsDto.setBuyerFirstName(product.getBuyer().getFirstName());
                                    soldProductsDto.setBuyerLastName(product.getBuyer().getLastName());

                                    soldProducts.add(soldProductsDto);
                                }
                            });

                    userProductsDto.setSoldProducts(soldProducts);

                    return userProductsDto;
                }).collect(Collectors.toList());

        return users.stream()
                .sorted(Comparator.comparing(UserWithSuccessfullySoldProductsDto::getLastName)
                        .thenComparing(UserWithSuccessfullySoldProductsDto::getFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public UsersAndProductsViewDto getUsersAndProducts() {
        UsersAndProductsViewDto usersAndProductsDto = new UsersAndProductsViewDto();
        usersAndProductsDto.setUsersCount(this.userRepository.findAllBySoldProductsSizeGreaterThan(1).size());

        List<UserViewDto> userViewDtos = this.userRepository
                .findAllBySoldProductsSizeGreaterThan(1)
                .stream()
                .map(user -> {

                    UserViewDto userViewDto = this.modelMapper.map(user, UserViewDto.class);

                    SoldProductsViewDto soldProductsViewDto = new SoldProductsViewDto();
                    soldProductsViewDto.setCount(user.getSoldProducts().size());

                    List<ProductsViewDto> productsViewDtos = user.getSoldProducts()
                            .stream()
                            .map(product -> this.modelMapper.map(product, ProductsViewDto.class))
                            .collect(Collectors.toList());

                    soldProductsViewDto.setProducts(productsViewDtos);
                    userViewDto.setSoldProducts(soldProductsViewDto);

                    return userViewDto;

                }).collect(Collectors.toList());

        userViewDtos = userViewDtos
                .stream()
                .sorted((u2, u1) -> {
                    int sort = Integer.compare(u2.getSoldProducts().getCount(), u1.getSoldProducts().getCount());
                    if (sort == 0) {
                        sort = u1.getLastName().compareTo(u2.getLastName());
                    }
                    return sort;
                }).collect(Collectors.toList());

        usersAndProductsDto.setUsers(userViewDtos);

        return usersAndProductsDto;
    }
}
