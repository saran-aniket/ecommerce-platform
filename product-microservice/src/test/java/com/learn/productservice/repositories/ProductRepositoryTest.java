// ProductRepositoryTest.java
package com.learn.productservice.repositories;

import com.learn.productservice.entities.Category;
import com.learn.productservice.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
    }

    @Test
    void testGetProductById() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);

        when(productRepository.getProductById(id)).thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.getProductById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(productRepository, times(1)).getProductById(id);
    }

    @Test
    void testGetProductByNameAndCategoryName() {
        String name = "Test Product";
        String categoryName = "Test Category";
        Product product = new Product();
        product.setName(name);
        Category category = new Category();
        category.setName(categoryName);
        product.setCategory(category);

        when(productRepository.getProductByNameAndCategory_Name(name, categoryName))
                .thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.getProductByNameAndCategory_Name(name, categoryName);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        assertEquals(categoryName, result.get().getCategory().getName());
        verify(productRepository, times(1))
                .getProductByNameAndCategory_Name(name, categoryName);
    }
}