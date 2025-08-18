package com.learn.productservice.services;

import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product saveProduct(ProductRequestDto productRequestDto);

    List<Product> getAllProducts(int pageNumber, int pageSize);

    Product getProductById(UUID id);
}
