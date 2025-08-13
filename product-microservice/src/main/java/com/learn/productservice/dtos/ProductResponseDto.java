package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private float price;
    private String image;
    private String category_name;
    private Long external_id;
}
