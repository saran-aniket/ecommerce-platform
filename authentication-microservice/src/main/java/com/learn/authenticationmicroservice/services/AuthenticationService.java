package com.learn.authenticationmicroservice.services;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.exceptions.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    GenericResponseDto<CustomerDto> signUp(CustomerUserSignupRequestDto customerSignRequestDto);

    GenericResponseDto<CustomerDto> login(UserLoginRequestDto customerLoginRequestDto);

    GenericResponseDto<Void> validateToken(String authHeader);

    GenericResponseDto<AuthenticationDto> getAuthentication(String authHeader);
}
