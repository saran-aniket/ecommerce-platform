package com.learn.authenticationmicroservice.services;

import com.learn.authenticationmicroservice.dtos.*;

public interface AuthenticationService {
    GenericResponseDto<UserDto> signUp(String roleType, UserSignupRequestDto customerSignupRequestDto);

    GenericResponseDto<UserDto> login(String roleType, UserLoginRequestDto customerLoginRequestDto);

    GenericResponseDto<AuthenticationDto> getAuthentication(String authHeader);

    void logout(String authHeader);
}
