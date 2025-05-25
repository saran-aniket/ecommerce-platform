package com.learn.fakestoremicroservice.mapper;

import com.learn.fakestoremicroservice.dto.FakeStoreProductResponseDto;
import com.learn.fakestoremicroservice.dto.ProductResponseDto;

public class ProductDtoMapper {
    public static ProductResponseDto toProductResponseDto(FakeStoreProductResponseDto fakeStoreProductResponseDto){
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(fakeStoreProductResponseDto.getId());
        productResponseDto.setName(fakeStoreProductResponseDto.getTitle());
        productResponseDto.setDescription(fakeStoreProductResponseDto.getDescription());
        productResponseDto.setPrice(fakeStoreProductResponseDto.getPrice());
        productResponseDto.setImage(fakeStoreProductResponseDto.getImage());
        productResponseDto.setCategory_name(fakeStoreProductResponseDto.getCategory());
        return productResponseDto;
    }
}
