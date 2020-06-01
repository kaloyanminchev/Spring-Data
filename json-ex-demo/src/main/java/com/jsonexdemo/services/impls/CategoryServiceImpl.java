package com.jsonexdemo.services.impls;

import com.jsonexdemo.models.dtos.queryDtos.query3.CategoriesByProductsViewDto;
import com.jsonexdemo.models.dtos.seedDtos.CategorySeedDto;
import com.jsonexdemo.models.entities.Category;
import com.jsonexdemo.models.entities.Product;
import com.jsonexdemo.repositories.CategoryRepository;
import com.jsonexdemo.services.CategoryService;
import com.jsonexdemo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories(CategorySeedDto[] categorySeedDtos) {
        Arrays.stream(categorySeedDtos)
                .forEach(categorySeedDto -> {
                    if (this.categoryRepository.findByName(categorySeedDto.getName()) != null) {
                        System.out.println(categorySeedDto.getName() + " already exists in database!");
                        return;
                    }

                    if (this.validationUtil.isValid(categorySeedDto)) {
                        Category category = this.modelMapper.map(categorySeedDto, Category.class);

                        this.categoryRepository.saveAndFlush(category);
                    } else {
                        this.validationUtil
                                .violations(categorySeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public List<Category> getRandomCategories() {
        List<Category> resultList = new ArrayList<>();
        Random random = new Random();
        int randomCounter = random.nextInt(3) + 1;

        for (int i = 0; i < randomCounter; i++) {
            long randomId = random.nextInt((int) this.categoryRepository.count()) + 1;
            resultList.add(this.categoryRepository.getOne(randomId));
        }

        return resultList;
    }

    @Override
    public List<CategoriesByProductsViewDto> getCategoriesByProducts() {
        List<CategoriesByProductsViewDto> categories =
                this.categoryRepository
                        .findAll()
                        .stream()
                        .map(category -> {

                            CategoriesByProductsViewDto categoriesByProductsViewDto =
                                    this.modelMapper.map(category, CategoriesByProductsViewDto.class);

                            int productsCount = category.getProducts().size();
                            categoriesByProductsViewDto.setProductsCount(productsCount);

                            BigDecimal totalPrice = new BigDecimal(0);
                            for (Product product : category.getProducts()) {
                                totalPrice = totalPrice.add(product.getPrice());
                            }

                            BigDecimal avgPrice =
                                    totalPrice.divide(BigDecimal.valueOf(productsCount), 6, RoundingMode.CEILING);

                            categoriesByProductsViewDto.setAveragePrice(avgPrice);
                            categoriesByProductsViewDto.setTotalRevenue(totalPrice);

                            return categoriesByProductsViewDto;
                        }).collect(Collectors.toList());

        return categories
                .stream()
                .sorted(Comparator.comparingInt(CategoriesByProductsViewDto::getProductsCount))
                .collect(Collectors.toList());
    }
}
