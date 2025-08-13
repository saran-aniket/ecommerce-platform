package com.learn.productservice.mapper;

import com.learn.productservice.dtos.FakeStoreProductResponse;
import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.dtos.ProductResponseDto;
import com.learn.productservice.model.Category;
import com.learn.productservice.model.Product;
import fakestoreproduct.ProductResponse;

public class ProductMapper {
    public static Product toProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setImage(productRequestDto.getImage());
        Category category = new Category();
        category.setName(productRequestDto.getCategory_name());
        product.setCategory(category);
        product.setExternal_id(productRequestDto.getExternal_id());
        return product;
    }

    public static ProductResponseDto toProductResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setImage(product.getImage());
        productResponseDto.setCategory_name(product.getCategory().getName());
        return productResponseDto;
    }

    public static FakeStoreProductResponse toFakeStoreProductResponseDto(ProductResponse productResponse) {
        FakeStoreProductResponse fakeStoreProductResponse = new FakeStoreProductResponse();
        fakeStoreProductResponse.setTitle(productResponse.getName());
        fakeStoreProductResponse.setDescription(productResponse.getDescription());
        fakeStoreProductResponse.setPrice(productResponse.getPrice());
        fakeStoreProductResponse.setImage(productResponse.getImage());
        fakeStoreProductResponse.setCategory(productResponse.getCategory());
        fakeStoreProductResponse.setId(productResponse.getId());
        return fakeStoreProductResponse;
    }

    public static ProductRequestDto toProductRequestDto(ProductResponseDto productResponseDto) {
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName(productResponseDto.getName());
        productRequestDto.setDescription(productResponseDto.getDescription());
        productRequestDto.setPrice(productResponseDto.getPrice());
        productRequestDto.setImage(productResponseDto.getImage());
        productRequestDto.setCategory_name(productResponseDto.getCategory_name());
        productRequestDto.setExternal_id(productResponseDto.getExternal_id());
        return productRequestDto;
    }

    public static ProductRequestDto toProductRequestDto(FakeStoreProductResponse fakeStoreProductResponse) {
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName(fakeStoreProductResponse.getTitle());
        productRequestDto.setDescription(fakeStoreProductResponse.getDescription());
        productRequestDto.setPrice(fakeStoreProductResponse.getPrice());
        productRequestDto.setImage(fakeStoreProductResponse.getImage());
        productRequestDto.setCategory_name(fakeStoreProductResponse.getCategory());
        productRequestDto.setExternal_id(fakeStoreProductResponse.getId());
        return productRequestDto;
    }
}
