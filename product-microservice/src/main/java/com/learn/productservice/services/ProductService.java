package com.learn.productservice.services;

import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.dtos.ProductResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDto saveProduct(ProductRequestDto productRequestDto);
    List<ProductResponseDto> getAllProducts(int pageNumber, int pageSize);
    ProductResponseDto getProductById(UUID id);
}
