package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductResponseDto {
    private String name;
    private String description;
    private float price;
    private String image;
    private String category_name;
}
