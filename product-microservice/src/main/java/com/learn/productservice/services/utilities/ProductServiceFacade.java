package com.learn.productservice.services.utilities;

import com.learn.productservice.client.UserServiceClient;
import com.learn.productservice.dtos.*;
import com.learn.productservice.entities.Category;
import com.learn.productservice.entities.Inventory;
import com.learn.productservice.entities.Product;
import com.learn.productservice.services.InventoryService;
import com.learn.productservice.services.JwtService;
import com.learn.productservice.services.ProductService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class ProductServiceUtility {

    Logger log = LoggerFactory.getLogger(ProductServiceUtility.class);
    private final UserServiceClient userServiceClient;
    private final Executor productDetailsExecutor;
    private final InventoryService inventoryService;
    private final ProductService productService;

    public ProductServiceUtility(UserServiceClient userServiceClient, Executor productDetailsExecutor, InventoryService inventoryService, ProductService productService) {
        this.userServiceClient = userServiceClient;
        this.productDetailsExecutor = productDetailsExecutor;
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    public ProductDetailsResponseDto getProductDetailsById(UUID id) {
        Product product = productService.getProductById(id);

        CompletableFuture<List<Inventory>> inventoryFuture = CompletableFuture.supplyAsync(
                () -> inventoryService.getInventory(id),
                productDetailsExecutor
        );

        CompletableFuture<Category> categoryFuture = CompletableFuture.supplyAsync(
                product::getCategory,
                productDetailsExecutor
        );

        CompletableFuture.allOf(inventoryFuture, categoryFuture).join();

        List<Inventory> inventories = inventoryFuture.join();

        Set<UUID> sellerIds = inventories.stream()
                .map(Inventory::getSeller_id)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));

        List<CompletableFuture<SellerApplicationUserDto>> allSellerFutures = sellerIds.stream()
                .map(sellerId -> CompletableFuture.supplyAsync(() -> fetchSellerById(sellerId)))
                .toList();

        CompletableFuture.allOf(allSellerFutures.toArray(new CompletableFuture[0])).join();

        List<SellerApplicationUserDto> sellers = allSellerFutures.stream()
                .map(CompletableFuture::join)
                .filter(java.util.Objects::nonNull)
                .toList();

        Category category = categoryFuture.join();

        List<ProductInventoryDetailsDto> inventoryDetails = inventories.stream()
                .map(inventory -> {
                    ProductInventoryDetailsDto dto = new ProductInventoryDetailsDto();
                    dto.setInventoryId(inventory.getId());
                    dto.setSellerId(inventory.getSeller_id());
                    dto.setQuantity(inventory.getQuantity());
                    return dto;
                })
                .toList();

        ProductDetailsResponseDto dto = new ProductDetailsResponseDto();
        dto.setProductId(product.getId().toString());
        dto.setProductName(product.getName());
        dto.setProductDescription(product.getDescription());
        dto.setProductPrice(product.getPrice());
        dto.setCategoryName(category != null ? category.getName() : null);
        dto.setInventories(inventoryDetails);
        dto.setSellers(sellers);

        return dto;
    }

    private SellerApplicationUserDto fetchSellerById(UUID sellerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwtToken = (String) authentication.getPrincipal();
            GenericResponseDto<GetUserResponseDto> response =
                    userServiceClient.getUserProfileById("Bearer "+jwtToken, "ROLE_SELLER", sellerId.toString());

            if (response == null || response.getData() == null) {
                return null;
            }

            GetUserResponseDto user = response.getData();
            SellerApplicationUserDto seller = new SellerApplicationUserDto();
            seller.setId(sellerId);
            seller.setFirstName(user.getFirstName());
            seller.setLastName(user.getLastName());
            seller.setEmail(user.getEmail());
            seller.setPhoneNumber(user.getPhoneNumber());
            seller.setCompanyName(user.getCompanyName());
            return seller;
        } catch (Exception e) {
            log.warn("Failed to fetch seller details for sellerId={}", sellerId, e);
            return null;
        }
    }
}
