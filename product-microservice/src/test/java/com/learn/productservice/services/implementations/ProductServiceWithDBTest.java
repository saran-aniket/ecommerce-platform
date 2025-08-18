package com.learn.productservice.services.implementations;

import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.exceptions.DuplicateProductFoundException;
import com.learn.productservice.exceptions.ProductNotFoundException;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.entities.Category;
import com.learn.productservice.entities.Product;
import com.learn.productservice.repositories.CategoryRepository;
import com.learn.productservice.repositories.ProductRepository;
import com.learn.productservice.services.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceWithDBTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ProductServiceWithDB productServiceWithDB;

    @Test
    void saveProduct_createsProductWhenCategoryNotExists() {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Product1");
        requestDto.setCategory_name("Category1");

        Product product = mock(Product.class);

        // Mock mapping and repo interaction
        try (MockedStatic<ProductMapper> mapperMockedStatic = mockStatic(ProductMapper.class)) {
            mapperMockedStatic.when(() -> ProductMapper.toProduct(requestDto)).thenReturn(product);
            when(categoryRepository.findByNameIgnoreCase("Category1")).thenReturn(Optional.empty());

            Category category = new Category();
            category.setName("Category1");
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(productRepository.save(product)).thenReturn(product);

            Product created = productServiceWithDB.saveProduct(requestDto);

            assertNotNull(created);
            verify(categoryRepository).save(any(Category.class));
            verify(productRepository).save(product);
        }
    }

    @Test
    void saveProduct_throwsExceptionIfProductAlreadyExists() {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("ExistProduct");
        requestDto.setCategory_name("ExistCat");

        Product product = mock(Product.class);
        when(product.getName()).thenReturn("ExistProduct");

        try (MockedStatic<ProductMapper> mapperMockedStatic = mockStatic(ProductMapper.class)) {
            mapperMockedStatic.when(() -> ProductMapper.toProduct(requestDto)).thenReturn(product);

            Category category = new Category();
            category.setName("ExistCat");
            when(categoryRepository.findByNameIgnoreCase("ExistCat")).thenReturn(Optional.of(category));
            when(productRepository.getProductByNameAndCategory_Name("ExistProduct", "ExistCat"))
                    .thenReturn(Optional.of(new Product()));

            assertThrows(DuplicateProductFoundException.class, () -> productServiceWithDB.saveProduct(requestDto));
        }
    }

    @Test
    void getAllProducts_returnsList() {
        List<Product> mockList = Arrays.asList(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(mockList);

        List<Product> result = productServiceWithDB.getAllProducts(1, 5);

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_returnsProductFromCacheIfExists() {
        UUID id = UUID.randomUUID();
        Product cachedProduct = new Product();
        String cacheKey = "PRODUCT_" + id;

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("PRODUCTS", cacheKey)).thenReturn(cachedProduct);

        Product result = productServiceWithDB.getProductById(id);

        assertSame(cachedProduct, result);
        verify(hashOperations).get("PRODUCTS", cacheKey);
        verify(productRepository, never()).getProductById(any());
    }

    @Test
    void getProductById_returnsProductFromDbIfNotInCache() {
        UUID id = UUID.randomUUID();
        String cacheKey = "PRODUCT_" + id;
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("PRODUCTS", cacheKey)).thenReturn(null);

        Product dbProduct = new Product();
        when(productRepository.getProductById(id)).thenReturn(Optional.of(dbProduct));

        Product result = productServiceWithDB.getProductById(id);

        assertSame(dbProduct, result);
        verify(hashOperations).put("PRODUCTS", cacheKey, dbProduct);
    }

    @Test
    void getProductById_throwsIfNotFound() {
        UUID id = UUID.randomUUID();
        String cacheKey = "PRODUCT_" + id;
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("PRODUCTS", cacheKey)).thenReturn(null);
        when(productRepository.getProductById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServiceWithDB.getProductById(id));
    }
}