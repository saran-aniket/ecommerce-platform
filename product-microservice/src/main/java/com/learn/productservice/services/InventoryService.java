package com.learn.productservice.services;

import com.learn.productservice.dtos.InventoryRequestDto;
import com.learn.productservice.entities.Inventory;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    void createInventory(InventoryRequestDto inventoryRequestDto);

    List<Inventory> getInventory(UUID productId);
}
