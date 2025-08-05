package com.learn.authenticationmicroservice.services;

import com.learn.authenticationmicroservice.dtos.*;

public interface AuthenticationService {
    GenericResponseDto<CustomerDto> signUp(CustomerUserSignupRequestDto customerSignRequestDto);

    GenericResponseDto<CustomerDto> login(UserLoginRequestDto customerLoginRequestDto);

//    GenericResponseDto<Void> validateToken(String authHeader);

    GenericResponseDto<AuthenticationDto> getAuthentication(String authHeader);
}
