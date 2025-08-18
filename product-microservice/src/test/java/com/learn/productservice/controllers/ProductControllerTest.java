package com.learn.productservice.controllers;

import com.learn.productservice.dtos.*;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.entities.Product;
import com.learn.productservice.services.InventoryService;
import com.learn.productservice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;
    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private ProductController productController;

    @Test
    void createProduct_shouldReturnOkAndMappedResponse() {
        ProductRequestDto requestDto = new ProductRequestDto();
        Product savedProduct = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();

        // Mock the service call and mapper
        when(productService.saveProduct(requestDto)).thenReturn(savedProduct);
        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            mockedMapper.when(() -> ProductMapper.toProductResponseDto(savedProduct)).thenReturn(responseDto);

            ResponseEntity<GenericResponseDto<ProductResponseDto>> response = productController.createProduct(requestDto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
            assertEquals(responseDto, response.getBody().getData());
        }
    }

    @Test
    void getAllProducts_shouldReturnListOfMappedProductDtos() {
        int pageNumber = 3;
        int pageSize = 10;
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = List.of(product1, product2);

        ProductResponseDto dto1 = new ProductResponseDto();
        ProductResponseDto dto2 = new ProductResponseDto();

        when(productService.getAllProducts(pageNumber, pageSize)).thenReturn(products);

        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            mockedMapper.when(() -> ProductMapper.toProductResponseDto(product1)).thenReturn(dto1);
            mockedMapper.when(() -> ProductMapper.toProductResponseDto(product2)).thenReturn(dto2);

            ResponseEntity<GenericResponseDto<List<ProductResponseDto>>> response =
                    productController.getAllProducts(pageNumber, pageSize);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
            List<ProductResponseDto> dtos = response.getBody().getData();
            assertNotNull(dtos);
            assertEquals(2, dtos.size());
            assertTrue(dtos.containsAll(List.of(dto1, dto2)));
        }
    }

    @Test
    void getAllProducts_shouldReturnEmptyListWhenNoProducts() {
        int pageNumber = 1;
        int pageSize = 5;

        when(productService.getAllProducts(pageNumber, pageSize)).thenReturn(Collections.emptyList());

        ResponseEntity<GenericResponseDto<List<ProductResponseDto>>> response =
                productController.getAllProducts(pageNumber, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        List<ProductResponseDto> dtos = response.getBody().getData();
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void getProductById_shouldReturnMappedProductDto() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();

        when(productService.getProductById(id)).thenReturn(product);

        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            mockedMapper.when(() -> ProductMapper.toProductResponseDto(product)).thenReturn(responseDto);

            ResponseEntity<GenericResponseDto<ProductResponseDto>> response =
                    productController.getProductById(id.toString());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
            assertEquals(responseDto, response.getBody().getData());
        }
    }

    @Test
    void getProductById_shouldThrowExceptionForInvalidUUID() {
        String invalidId = "not-a-uuid";
        assertThrows(IllegalArgumentException.class, () -> productController.getProductById(invalidId));
    }

    @Test
    void createInventory_shouldReturnCreatedResponseAndCallService() {
        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto();

        doNothing().when(inventoryService).createInventory(inventoryRequestDto);

        ResponseEntity<GenericResponseDto<Void>> response = productController.createInventory(inventoryRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        assertNull(response.getBody().getData());

        verify(inventoryService, times(1)).createInventory(inventoryRequestDto);
    }
}