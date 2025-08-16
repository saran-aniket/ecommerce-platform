package com.learn.productservice.services;

import com.learn.productservice.dtos.InventoryRequestDto;

public interface InventoryService {

    void createInventory(InventoryRequestDto inventoryRequestDto);
}
