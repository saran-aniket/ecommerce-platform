package com.learn.productservice.controllers;

import com.learn.productservice.dtos.GenericResponseDto;
import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.dtos.ProductResponseDto;
import com.learn.productservice.dtos.ResponseStatus;
import com.learn.productservice.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(@Qualifier("productServiceWithDb") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<GenericResponseDto<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto productRequestDto){
        log.info("Received request to save product {}", productRequestDto);
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                productService.saveProduct(productRequestDto)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<List<ProductResponseDto>>> getAllProducts(@RequestParam(defaultValue =
                                                                                                   "1") int pageNumber,
                                                                   @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", productService.getAllProducts(pageNumber, pageSize)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<ProductResponseDto>> getProductById(@PathVariable("id") String id){
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", productService.getProductById(UUID.fromString(id))));
    }
}
