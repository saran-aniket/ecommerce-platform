package com.learn.productservice.services.implementations;

import com.learn.productservice.client.UserServiceClient;
import com.learn.productservice.dtos.GetUserResponseDto;
import com.learn.productservice.dtos.InventoryRequestDto;
import com.learn.productservice.exceptions.UserNotFoundException;
import com.learn.productservice.entities.Inventory;
import com.learn.productservice.entities.Product;
import com.learn.productservice.repositories.InventoryRepository;
import com.learn.productservice.services.InventoryService;
import com.learn.productservice.services.ProductService;
import com.learn.productservice.services.utilities.PSConstants;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;
    private final UserServiceClient userServiceClient;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductService productService, UserServiceClient userServiceClient) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void createInventory(InventoryRequestDto inventoryRequestDto) {
        Product product = productService.getProductById(inventoryRequestDto.getProduct_id());
        GetUserResponseDto getUserResponseDto = userServiceClient.getUserProfileById(PSConstants.SELLER_ROLE,
                String.valueOf(inventoryRequestDto.getSeller_id())).getData();
        if (getUserResponseDto == null) {
            throw new UserNotFoundException("User not found");
        }
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryRequestDto.getQuantity());
        inventory.setSeller_id(inventoryRequestDto.getSeller_id());
        inventoryRepository.save(inventory);
    }
}
