package com.learn.fakestoremicroservice.service;

import com.learn.fakestoremicroservice.dto.FakeStoreProductResponseDto;
import com.learn.fakestoremicroservice.dto.ProductResponseDto;
import com.learn.fakestoremicroservice.mapper.ProductDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService {
    private static final Logger log = LoggerFactory.getLogger(FakeStoreProductService.class);
    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductResponseDto> getAllProducts(int pageNumber, int pageSize){
        final String url = "https://fakestoreapi.com/products";
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        FakeStoreProductResponseDto[] fakeStoreProductResponseDtoList =
                restTemplate.getForObject(url,
                FakeStoreProductResponseDto[].class);

        if(fakeStoreProductResponseDtoList != null) {
            log.info("Received FakeStoreProductResponseDto list {}", fakeStoreProductResponseDtoList.toString());
            int start = (pageNumber - 1) * pageSize;
            int end = pageNumber * pageSize;
            if(end > fakeStoreProductResponseDtoList.length){
                end = fakeStoreProductResponseDtoList.length;
            }

            while(start < end){
                productResponseDtoList.add(ProductDtoMapper.toProductResponseDto(fakeStoreProductResponseDtoList[start]));
                start++;
            }

            log.info("Sending FakeStoreProductResponseDto list {}", productResponseDtoList.toString());
        }
        return productResponseDtoList;
    }
}
