package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDetailsResponseDto {
    private String productId;
    private String productName;
    private String productDescription;
    private float productPrice;
    private String categoryName;
    private List<ProductInventoryDetailsDto> inventories;
    private List<SellerApplicationUserDto> sellers;
}
