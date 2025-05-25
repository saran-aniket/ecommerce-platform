package com.learn.productservice.service;

import com.learn.productservice.dto.ProductRequestDto;
import com.learn.productservice.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto saveProduct(ProductRequestDto productRequestDto);
    List<ProductResponseDto> getAllProducts(int pageNumber, int pageSize);
}
