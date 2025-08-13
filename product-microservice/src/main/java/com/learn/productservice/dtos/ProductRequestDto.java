package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String name;
    private String description;
    private float price;
    private String image;
    private String category_name;
    private Long external_id;

}
