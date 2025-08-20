package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.SellerNotFoundException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Seller;
import com.learn.usermicroservice.repositories.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerUserProfileServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private SellerUserProfileServiceImpl sellerUserProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserProfile_shouldSaveAndReturnSeller() {
        UserSignupRequestDto signupDto = new UserSignupRequestDto();
        signupDto.setFirstName("Alice");
        signupDto.setLastName("Wonder");
        signupDto.setPhoneNumber("1234509876");
        signupDto.setEmail("alice@sellers.com");
        signupDto.setPassword("pwd12345");
        signupDto.setCompanyName("WonderCorp");

        ApplicationUser user = new ApplicationUser();

        Seller savedSeller = new Seller();
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPwd");
        when(sellerRepository.save(any(Seller.class))).thenReturn(savedSeller);

        Seller result = sellerUserProfileService.createUserProfile(signupDto, user);

        assertNotNull(result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
        verify(bCryptPasswordEncoder, times(1)).encode("pwd12345");
    }

    @Test
    void updateUserProfile_shouldUpdateAndReturnSeller() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setFirstName("Bob");
        updateDto.setLastName("Builder");
        updateDto.setPhoneNumber("44220011");
        updateDto.setCompanyName("BuildersInc");

        ApplicationUser user = new ApplicationUser();
        user.setEmail("bob@sellers.com");

        Seller existingSeller = new Seller();
        existingSeller.setEmail("bob@sellers.com");

        when(sellerRepository.findSellerByEmail("bob@sellers.com"))
                .thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(existingSeller);

        Seller result = sellerUserProfileService.updateUserProfile(updateDto, user);

        assertNotNull(result);
        assertEquals("Bob", result.getFirstName());
        assertEquals("Builder", result.getLastName());
        assertEquals("44220011", result.getPhoneNumber());
        assertEquals("BuildersInc", result.getCompanyName());
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    void getUserProfileByApplicationUser_shouldReturnSeller() {
        String email = "seller@example.com";
        Seller seller = new Seller();
        seller.setEmail(email);

        when(sellerRepository.findSellerByEmail(email)).thenReturn(Optional.of(seller));

        Seller result = sellerUserProfileService.getUserProfileByApplicationUser(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserProfileByApplicationUser_shouldThrowExceptionIfNotFound() {
        String email = "noseller@example.com";
        when(sellerRepository.findSellerByEmail(email)).thenReturn(Optional.empty());

        assertThrows(SellerNotFoundException.class, () -> {
            sellerUserProfileService.getUserProfileByApplicationUser(email);
        });
    }
}