package com.learn.productservice.services.implementations;

import com.learn.productservice.dtos.ProductRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class GetFakeStoreProductScheduler {
    private static final Logger log = LoggerFactory.getLogger(GetFakeStoreProductScheduler.class);
    private final FakeStoreProductService productServiceFakeStore;
    private final ProductServiceWithDB productServiceMysql;
    public static int pageNumber = 1;
    public static int pageSize = 5;

    public GetFakeStoreProductScheduler(FakeStoreProductService productServiceFakeStore, ProductServiceWithDB productServiceMysql) {
        this.productServiceFakeStore = productServiceFakeStore;
        this.productServiceMysql = productServiceMysql;
    }

//    @Scheduled(cron = "0 */5 * * * *")
    public void getFakeStoreProducts(){
        log.info("Not Fetching products from FakeStoreProductService");
//        List<FakeStoreProductResponse> fakeStoreProductResponseList = productServiceFakeStore.getAllProducts(pageNumber, pageSize);
//        List<ProductRequestDto> productRequestDtoList =
//                fakeStoreProductResponseList.stream().map(ProductMapper::toProductRequestDto).toList();
//        saveAllProduct(productRequestDtoList);
//        pageNumber++;
    }

//    public void saveAllProduct(List<ProductRequestDto> productRequestDtoList){
//        log.info("Saving products to database");
//        productServiceMysql.saveAllProducts(productRequestDtoList);
//    }
}
