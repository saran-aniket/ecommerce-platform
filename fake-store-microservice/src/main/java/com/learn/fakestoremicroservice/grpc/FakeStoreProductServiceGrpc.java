package com.learn.fakestoremicroservice.grpc;

import com.learn.fakestoremicroservice.dto.ProductResponseDto;
import com.learn.fakestoremicroservice.service.FakeStoreProductService;
import fakestoreproduct.ProductRequest;
import fakestoreproduct.ProductResponse;
import fakestoreproduct.ProductResponseList;
import fakestoreproduct.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@GrpcService
public class FakeStoreProductServiceGrpc extends ProductServiceGrpc.ProductServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(FakeStoreProductServiceGrpc.class);
    private final FakeStoreProductService fakeStoreProductService;

    public FakeStoreProductServiceGrpc(FakeStoreProductService fakeStoreProductService) {
        this.fakeStoreProductService = fakeStoreProductService;
    }

    public void getFakeStoreProducts(ProductRequest productRequest
            , StreamObserver<ProductResponseList> productResponseListStreamObserver) {
        log.info("Received getFakeStoreProducts request");

        List<ProductResponseDto> productResponseDtoList =
                fakeStoreProductService.getAllProducts(productRequest.getPageNumber(), productRequest.getPageSize());

        ProductResponseList.Builder productResponseListBuilder = ProductResponseList.newBuilder();

        for (ProductResponseDto productResponseDto : productResponseDtoList) {
            ProductResponse productResponse = ProductResponse.newBuilder()
                    .setName(productResponseDto.getName())
                    .setCategory(productResponseDto.getCategory_name())
                    .setDescription(productResponseDto.getDescription())
                    .setPrice(productResponseDto.getPrice())
                    .setImage(productResponseDto.getImage())
                    .setId(productResponseDto.getId())
                    .build();
            productResponseListBuilder.addProductResponse(productResponse);
        }
        ProductResponseList productResponseList = productResponseListBuilder.build();

        log.info("Sending getFakeStoreProducts response {}", productResponseList);

        productResponseListStreamObserver.onNext(productResponseList);
        productResponseListStreamObserver.onCompleted();


    }
}
