package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.CustomerNotFoundException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerUserProfileServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private CustomerUserProfileServiceImpl customerUserProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserProfile_shouldSaveAndReturnCustomer() {
        UserSignupRequestDto signupDto = new UserSignupRequestDto();
        signupDto.setFirstName("John");
        signupDto.setLastName("Doe");
        signupDto.setPhoneNumber("1234567890");
        signupDto.setEmail("john@example.com");
        signupDto.setPassword("password");

        ApplicationUser user = new ApplicationUser();
        user.setUserRoles(List.of(new UserRole()));

        Customer savedCustomer = new Customer();
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerUserProfileService.createUserProfile(signupDto, user);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(bCryptPasswordEncoder, times(1)).encode("password");
    }

    @Test
    void updateUserProfile_shouldUpdateAndReturnCustomer() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setPhoneNumber("9988776655");

        ApplicationUser user = new ApplicationUser();
        user.setEmail("jane@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setEmail("jane@example.com");

        when(customerRepository.findCustomerByEmail("jane@example.com"))
                .thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer result = customerUserProfileService.updateUserProfile(updateDto, user);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("9988776655", result.getPhoneNumber());
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void getUserProfileByApplicationUser_shouldReturnCustomer() {
        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);

        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.of(customer));

        Customer result = customerUserProfileService.getUserProfileByApplicationUser(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserProfileByApplicationUser_shouldThrowExceptionIfNotFound() {
        String email = "notfound@example.com";
        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerUserProfileService.getUserProfileByApplicationUser(email);
        });
    }
}