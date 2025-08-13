package com.learn.productservice.services.implementations;

import org.springframework.stereotype.Service;

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
