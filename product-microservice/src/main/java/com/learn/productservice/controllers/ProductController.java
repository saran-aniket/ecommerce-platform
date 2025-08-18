package com.learn.productservice.controllers;

import com.learn.productservice.dtos.*;
import com.learn.productservice.dtos.ResponseStatus;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.services.InventoryService;
import com.learn.productservice.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
    private final InventoryService inventoryService;

    public ProductController(@Qualifier("productServiceWithDb") ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<GenericResponseDto<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        log.info("Received request to save product {}", productRequestDto);
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                ProductMapper.toProductResponseDto(productService.saveProduct(productRequestDto))));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<List<ProductResponseDto>>> getAllProducts(@RequestParam(defaultValue =
                                                                                               "1") int pageNumber,
                                                                                       @RequestParam(defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                productService.getAllProducts(pageNumber, pageSize).stream().map(ProductMapper::toProductResponseDto).toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<ProductResponseDto>> getProductById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                ProductMapper.toProductResponseDto(productService.getProductById(UUID.fromString(id)))));
    }

    @PostMapping("/inventory")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<GenericResponseDto<Void>> createInventory(@RequestBody InventoryRequestDto inventoryRequestDto) {
        log.info("Received request to save product {}", inventoryRequestDto);
        inventoryService.createInventory(inventoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                null));
    }
}
