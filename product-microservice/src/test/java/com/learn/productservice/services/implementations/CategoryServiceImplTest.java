package com.learn.productservice.services.implementations;

import com.learn.productservice.entities.Category;
import com.learn.productservice.exceptions.DuplicateCategoryFoundException;
import com.learn.productservice.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testCreateCategory_Success() {
        String categoryName = "Electronics";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(categoryName);

        assertNotNull(result);
        assertEquals(categoryName, result.getName());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategory_DuplicateCategory() {
        String categoryName = "Electronics";
        Category existingCategory = new Category();
        existingCategory.setName(categoryName);

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.of(existingCategory));

        assertThrows(DuplicateCategoryFoundException.class, () -> {
            categoryService.createCategory(categoryName);
        });

        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetCategoryByName_Found() {
        String categoryName = "Electronics";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryByName(categoryName);

        assertTrue(result.isPresent());
        assertEquals(categoryName, result.get().getName());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
    }

    @Test
    void testGetCategoryByName_NotFound() {
        String categoryName = "NonExistent";

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryByName(categoryName);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
    }

}
