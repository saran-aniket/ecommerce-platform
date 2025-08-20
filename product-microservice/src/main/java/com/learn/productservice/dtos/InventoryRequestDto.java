package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InventoryRequestDto {
    private UUID product_id;
    private int quantity;
}
