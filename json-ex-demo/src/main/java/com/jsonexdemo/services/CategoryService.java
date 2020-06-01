package com.jsonexdemo.services;

import com.jsonexdemo.models.dtos.queryDtos.query3.CategoriesByProductsViewDto;
import com.jsonexdemo.models.dtos.seedDtos.CategorySeedDto;
import com.jsonexdemo.models.entities.Category;

import java.util.List;

public interface CategoryService {
    void seedCategories(CategorySeedDto[] categorySeedDtos);

    List<Category> getRandomCategories();

    List<CategoriesByProductsViewDto> getCategoriesByProducts();
}
