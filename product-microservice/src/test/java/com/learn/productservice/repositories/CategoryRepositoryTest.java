package com.learn.productservice.repositories;

import com.learn.productservice.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
    }

    @Test
    void testFindByNameIgnoreCase() {
        // Arrange
        String name = "Electronics";
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        category.setName(name);
        category.setId(uuid);

        when(categoryRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = categoryRepository.findByNameIgnoreCase(name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    void testFindByNameIgnoreCase_NotFound() {
        String name = "NonExistent";
        when(categoryRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        Optional<Category> result = categoryRepository.findByNameIgnoreCase(name);

        assertFalse(result.isPresent());
    }
}