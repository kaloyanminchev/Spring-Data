package com.softuni.springdataintroex.services;

import com.softuni.springdataintroex.entities.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Category getCategoryById(Long id);

    Set<Category> getRandomCategories();
}
