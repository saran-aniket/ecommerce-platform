package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.utility.TestDataSetup;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private ApplicationUser applicationUser;
    private static final UUID APPLICATION_USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        customer = TestDataSetup.getSingleCustomer();
        applicationUser = customer.getApplicationUser();
        applicationUser.setId(APPLICATION_USER_ID);
    }

    @Test
    void findCustomerByApplicationUser_Success() {
        Mockito.when(customerRepository.findCustomerByApplicationUser(applicationUser)).thenReturn(Optional.ofNullable(customer));

        Optional<Customer> customer1 = customerRepository.findCustomerByApplicationUser(applicationUser);
        Mockito.verify(customerRepository).findCustomerByApplicationUser(applicationUser);

        assertTrue(customer1.isPresent());
        assertEquals(customer.getFirstName(), customer1.get().getFirstName());
    }

    @Test
    void findCustomerByApplicationUser_Failure() {
        Mockito.when(customerRepository.findCustomerByApplicationUser(applicationUser)).thenReturn(Optional.empty());

        Optional<Customer> customer1 = customerRepository.findCustomerByApplicationUser(applicationUser);
        Mockito.verify(customerRepository).findCustomerByApplicationUser(applicationUser);

        assertTrue(customer1.isEmpty());
    }
}