package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.Seller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SellerRepositoryTest {

    @Mock
    private SellerRepository sellerRepository;

    @Test
    void findSellerByEmail_notExists_returnsEmpty() {
        Optional<Seller> seller = sellerRepository.findSellerByEmail("noseller@example.com");
        assertTrue(seller.isEmpty());
    }

    @Test
    void findSellerByEmail_exists_returnsSeller() {
        Seller seller = new Seller();
        seller.setEmail("seller1@example.com");
        seller.setCompanyName("Test Company");
        seller.setPassword("password");
        seller.setActive(true);

        when(sellerRepository.findSellerByEmail(anyString())).thenReturn(Optional.of(seller));

        Optional<Seller> found = sellerRepository.findSellerByEmail("seller1@example.com");
        assertTrue(found.isPresent());
        assertEquals(seller.getEmail(), found.get().getEmail());
        assertEquals(seller.getCompanyName(), found.get().getCompanyName());
    }
}