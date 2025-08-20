package com.learn.productservice.services.implementations;

import com.learn.productservice.client.UserServiceClient;
import com.learn.productservice.dtos.GetUserResponseDto;
import com.learn.productservice.dtos.InventoryRequestDto;
import com.learn.productservice.entities.Inventory;
import com.learn.productservice.entities.Product;
import com.learn.productservice.exceptions.UserNotFoundException;
import com.learn.productservice.repositories.InventoryRepository;
import com.learn.productservice.services.InventoryService;
import com.learn.productservice.services.ProductService;
import com.learn.productservice.services.utilities.PSConstants;
import com.learn.productservice.services.utilities.ProductServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;
    private final UserServiceClient userServiceClient;
    private final ProductServiceUtility productServiceUtility;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductService productService, UserServiceClient userServiceClient, ProductServiceUtility productServiceUtility) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
        this.userServiceClient = userServiceClient;
        this.productServiceUtility = productServiceUtility;
    }

    @Override
    public void createInventory(InventoryRequestDto inventoryRequestDto) {
        log.info("Creating inventory for product id {} ", inventoryRequestDto.getProduct_id());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken = (String) authentication.getPrincipal();
        String sellerId = productServiceUtility.getUserIdFromToken(jwtToken);
        log.info("Seller id from token {}", sellerId);
        Product product = productService.getProductById(inventoryRequestDto.getProduct_id());
        GetUserResponseDto getUserResponseDto = userServiceClient.getUserProfileById("Bearer " + jwtToken, PSConstants.SELLER_ROLE,
                sellerId).getData();
        if (getUserResponseDto == null) {
            throw new UserNotFoundException("User not found");
        }
        log.info("User found for seller id {}", sellerId);
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryRequestDto.getQuantity());
        inventory.setSeller_id(UUID.fromString(sellerId));
        inventoryRepository.save(inventory);
    }
}
