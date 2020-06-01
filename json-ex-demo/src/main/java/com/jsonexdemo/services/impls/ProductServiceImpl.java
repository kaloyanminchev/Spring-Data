package com.jsonexdemo.services.impls;

import com.jsonexdemo.models.dtos.queryDtos.query1.ProductsInRangeViewDto;
import com.jsonexdemo.models.dtos.seedDtos.ProductSeedDto;
import com.jsonexdemo.models.entities.Product;
import com.jsonexdemo.repositories.ProductRepository;
import com.jsonexdemo.services.CategoryService;
import com.jsonexdemo.services.ProductService;
import com.jsonexdemo.services.UserService;
import com.jsonexdemo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts(ProductSeedDto[] productSeedDtos) {
        Arrays.stream(productSeedDtos)
                .forEach(productSeedDto -> {

                    if (this.productRepository.findByName(productSeedDto.getName()) != null) {
                        System.out.println(productSeedDto.getName() + " already exists in database!");
                        return;
                    }

                    if (this.validationUtil.isValid(productSeedDto)) {
                        Product product = this.modelMapper.map(productSeedDto, Product.class);

                        Random random = new Random();
                        int randomNum = random.nextInt(2);
                        if (randomNum == 1) {
                            product.setBuyer(this.userService.getRandomUser());
                        }

                        product.setSeller(this.userService.getRandomUser());
                        product.setCategories(new HashSet<>(this.categoryService.getRandomCategories()));

                        this.productRepository.saveAndFlush(product);
                    } else {
                        this.validationUtil
                                .violations(productSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public List<ProductsInRangeViewDto> getAllProductsInRange() {
        List<ProductsInRangeViewDto> products = this.productRepository
                .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500), BigDecimal.valueOf(1000))
                .stream()
                .map(p -> {
                    ProductsInRangeViewDto productInRangeDto =
                            this.modelMapper.map(p, ProductsInRangeViewDto.class);

                    productInRangeDto
                            .setSeller(String.format("%s %s",
                                    p.getSeller().getFirstName(),
                                    p.getSeller().getLastName()));

                    return productInRangeDto;
                })
                .collect(Collectors.toList());

        return products.stream()
                .sorted(Comparator.comparing(ProductsInRangeViewDto::getPrice))
                .collect(Collectors.toList());
    }
}
