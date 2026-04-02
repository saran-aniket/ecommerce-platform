package com.learn.productservice.services.implementations;

import com.learn.productservice.entities.Category;
import com.learn.productservice.exceptions.DuplicateCategoryFoundException;
import com.learn.productservice.repositories.CategoryRepository;
import com.learn.productservice.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String categoryName) {
        if(getCategoryByName(categoryName).isPresent()){
            throw new DuplicateCategoryFoundException("Category already exists");
        }
        Category category = new Category();
        category.setName(categoryName);
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepository.findByNameIgnoreCase(categoryName);
    }
}
