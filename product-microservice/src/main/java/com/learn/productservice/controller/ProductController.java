package com.learn.productservice.controller;

import com.learn.productservice.dto.ProductRequestDto;
import com.learn.productservice.dto.ProductResponseDto;
import com.learn.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.learn.productservice.service.ProductServiceMySql;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private ProductServiceMySql productServiceMySql;

    public ProductController(ProductServiceMySql productServiceMySql) {
        this.productServiceMySql = productServiceMySql;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto){
        return ResponseEntity.ok(productServiceMySql.saveProduct(productRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@RequestParam(defaultValue = "1") int pageNumber,
                                                                   @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.ok(productServiceMySql.getAllProducts(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("id") String id){
        return ResponseEntity.ok(productServiceMySql.getProductById(UUID.fromString(id)));
    }
}
