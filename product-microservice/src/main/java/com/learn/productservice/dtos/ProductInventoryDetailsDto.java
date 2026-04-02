package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductInventoryDetailsDto {
    private UUID inventoryId;
    private UUID sellerId;
    private int quantity;
}
