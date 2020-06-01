package com.jsonexdemo.controllers;

import com.google.gson.Gson;
import com.jsonexdemo.models.dtos.queryDtos.query3.CategoriesByProductsViewDto;
import com.jsonexdemo.models.dtos.queryDtos.query1.ProductsInRangeViewDto;
import com.jsonexdemo.models.dtos.queryDtos.query2.UserWithSuccessfullySoldProductsDto;
import com.jsonexdemo.models.dtos.queryDtos.query4.UsersAndProductsViewDto;
import com.jsonexdemo.models.dtos.seedDtos.CategorySeedDto;
import com.jsonexdemo.models.dtos.seedDtos.ProductSeedDto;
import com.jsonexdemo.models.dtos.seedDtos.UserSeedDto;
import com.jsonexdemo.services.CategoryService;
import com.jsonexdemo.services.ProductService;
import com.jsonexdemo.services.UserService;
import com.jsonexdemo.utils.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.jsonexdemo.constants.GlobalConstants.*;

@Component
public class AppController implements CommandLineRunner {
    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final FileIOUtil fileIOUtil;

    @Autowired
    public AppController(Gson gson,
                         CategoryService categoryService,
                         UserService userService,
                         ProductService productService,
                         FileIOUtil fileIOUtil) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.fileIOUtil = fileIOUtil;
    }

    @Override
    public void run(String... args) throws Exception {
//        this.seedDatabase();
//        this.productsInRange();
//        this.usersWithProducts();
//        this.categoriesByProducts();
        this.getUsersAndProducts();
    }

    private void getUsersAndProducts() throws IOException {
        UsersAndProductsViewDto usersAndProductsDto = this.userService.getUsersAndProducts();

        String json = this.gson.toJson(usersAndProductsDto);
        this.fileIOUtil.write(json, QUERY_4_OUTPUT);
    }

    private void categoriesByProducts() throws IOException {
        List<CategoriesByProductsViewDto> categories = this.categoryService.getCategoriesByProducts();

        String json = this.gson.toJson(categories);
        this.fileIOUtil.write(json, QUERY_3_OUTPUT);
    }

    private void usersWithProducts() throws IOException {
        List<UserWithSuccessfullySoldProductsDto> usersByProducts = this.userService.getUsersByProducts(1);

        String json = this.gson.toJson(usersByProducts);
        this.fileIOUtil.write(json, QUERY_2_OUTPUT);
    }

    private void productsInRange() throws IOException {
        List<ProductsInRangeViewDto> dtos = this.productService.getAllProductsInRange();

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, QUERY_1_OUTPUT);
    }

    private void seedDatabase() throws IOException {
        this.seedCategories();
        this.seedUsers();
        this.seedProducts();
    }

    private void seedCategories() throws IOException {
        CategorySeedDto[] dtos = this.gson
                .fromJson(new FileReader(CATEGORIES_FILE_PATH),
                        CategorySeedDto[].class);

        this.categoryService.seedCategories(dtos);
    }

    private void seedUsers() throws IOException {
        UserSeedDto[] userSeedDtos = this.gson
                .fromJson(new FileReader(USERS_FILE_PATH),
                        UserSeedDto[].class);

        this.userService.seedUsers(userSeedDtos);
    }

    private void seedProducts() throws IOException {
        ProductSeedDto[] productSeedDtos = this.gson
                .fromJson(new FileReader(PRODUCTS_FILE_PATH),
                        ProductSeedDto[].class);

        this.productService.seedProducts(productSeedDtos);
    }
}
