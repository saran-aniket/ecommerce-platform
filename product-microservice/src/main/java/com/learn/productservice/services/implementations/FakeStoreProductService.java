package com.learn.productservice.services.implementations;

import com.learn.productservice.dtos.FakeStoreProductResponse;
import com.learn.productservice.grpc.FakeStoreProductServiceClient;
import com.learn.productservice.mapper.ProductMapper;
import fakestoreproduct.ProductResponse;
import fakestoreproduct.ProductResponseList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "fakeStoreProductService")
public class FakeStoreProductService {
//    private final FakeStoreProductServiceClient fakeStoreProductServiceClient;
//
//    public FakeStoreProductService(FakeStoreProductServiceClient fakeStoreProductServiceClient) {
//        this.fakeStoreProductServiceClient = fakeStoreProductServiceClient;
//    }

//    public List<FakeStoreProductResponse> getAllProducts(int pageNumber, int pageSize) {
//        ProductResponseList productResponseList = fakeStoreProductServiceClient.getFakeStoreProducts(pageNumber, pageSize);
//        List<FakeStoreProductResponse> fakeStoreProductResponseList = new ArrayList<>();
//        for (ProductResponse productResponse : productResponseList.getProductResponseList()) {
//            FakeStoreProductResponse productResponseDto = ProductMapper.toFakeStoreProductResponseDto(productResponse);
//            fakeStoreProductResponseList.add(productResponseDto);
//        }
//        return fakeStoreProductResponseList;
//    }
}
