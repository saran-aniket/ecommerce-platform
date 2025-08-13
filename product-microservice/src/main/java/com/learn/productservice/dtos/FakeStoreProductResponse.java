package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FakeStoreProductResponse {
    private long id;
    private String title;
    private String description;
    private float price;
    private String image;
    private String category;
}
