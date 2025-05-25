package com.learn.productservice.grpc;

import fakestoreproduct.ProductRequest;
import fakestoreproduct.ProductResponseList;
import fakestoreproduct.ProductServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FakeStoreProductServiceClient {
    private static final Logger log = LoggerFactory.getLogger(FakeStoreProductServiceClient.class);
    private final ProductServiceGrpc.ProductServiceBlockingStub blockingStub;

    public FakeStoreProductServiceClient(@Value("${fakestore.grpc.host:localhost}") String host, @Value("${fakestore" +
            ".grpc.port:9000}") int port) {
        log.info("Sending connection request to host {} and port {}", host, port);
        blockingStub =ProductServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        );

        log.info("Connected successfully");
    }

    public ProductResponseList getFakeStoreProducts(int pageNumber, int pageSize){
        ProductResponseList productResponseList = blockingStub.getFakeStoreProducts(ProductRequest.newBuilder()
                .setPageNumber(pageNumber)
                .setPageSize(pageSize)
                .build());

        log.info("Received GetFakeStoreProducts response {}", productResponseList);
        return productResponseList;
    }
}
