package com.learn.productservice.services.implementations;

import com.learn.productservice.client.UserServiceClient;
import com.learn.productservice.dtos.GenericResponseDto;
import com.learn.productservice.dtos.GetUserResponseDto;
import com.learn.productservice.dtos.InventoryRequestDto;
import com.learn.productservice.dtos.ResponseStatus;
import com.learn.productservice.entities.Inventory;
import com.learn.productservice.entities.Product;
import com.learn.productservice.repositories.InventoryRepository;
import com.learn.productservice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void testCreateInventory() {
        UUID productId = UUID.randomUUID();
        UUID sellerId = UUID.randomUUID();
        Product product = new Product();

        InventoryRequestDto requestDto = new InventoryRequestDto();

        Inventory inventory = new Inventory();
        inventory.setSeller_id(sellerId);
        inventory.setProduct(product);
        inventory.setQuantity(10);

        GenericResponseDto<GetUserResponseDto> userResponseDtoGenericResponseDto =
                GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", new GetUserResponseDto());

        when(inventoryRepository.save(any())).thenReturn(inventory);
        when(userServiceClient.getUserProfileById(anyString(), anyString())).thenReturn(userResponseDtoGenericResponseDto);

        inventoryService.createInventory(requestDto);

        verify(inventoryRepository, times(1)).save(any());
    }
}