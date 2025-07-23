package com.learn.productservice.service;

import com.learn.productservice.dto.FakeStoreProductResponse;
import com.learn.productservice.dto.ProductRequestDto;
import com.learn.productservice.dto.ProductResponseDto;
import com.learn.productservice.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class GetFakeStoreProductScheduler {
    private static final Logger log = LoggerFactory.getLogger(GetFakeStoreProductScheduler.class);
    private final FakeStoreProductService productServiceFakeStore;
    private final ProductServiceMySql productServiceMysql;
    public static int pageNumber = 1;
    public static int pageSize = 5;

    public GetFakeStoreProductScheduler(FakeStoreProductService productServiceFakeStore, ProductServiceMySql productServiceMysql) {
        this.productServiceFakeStore = productServiceFakeStore;
        this.productServiceMysql = productServiceMysql;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void getFakeStoreProducts(){
        log.info("Not Fetching products from FakeStoreProductService");
//        List<FakeStoreProductResponse> fakeStoreProductResponseList = productServiceFakeStore.getAllProducts(pageNumber, pageSize);
//        List<ProductRequestDto> productRequestDtoList =
//                fakeStoreProductResponseList.stream().map(ProductMapper::toProductRequestDto).toList();
//        saveAllProduct(productRequestDtoList);
//        pageNumber++;
    }

    public void saveAllProduct(List<ProductRequestDto> productRequestDtoList){
        log.info("Saving products to database");
        productServiceMysql.saveAllProducts(productRequestDtoList);
    }
}
