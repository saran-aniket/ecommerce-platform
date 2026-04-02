package com.learn.productservice.services;

import com.learn.productservice.entities.Category;

import java.util.Optional;

public interface CategoryService {
    Category createCategory(String categoryName);
    Optional<Category> getCategoryByName(String categoryName);
}
