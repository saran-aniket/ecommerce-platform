package com.learn.usermicroservice.services;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.Customer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(String firstName, String lastName, String email, String password, String phoneNumber);
    Customer updateCustomer(String firstName, String lastName, String email, String phoneNumber);
    void deleteCustomer(String email);
    Optional<Customer> getCustomerByEmail(String email) throws InvalidCredentialException;

    Token login(String email, String password) throws InvalidCredentialException;
    void logout(String token);

    void validateToken(String token);

    AuthenticationDto getAuthentication(String authorizationHeader);

}
