package com.learn.authenticationmicroservice.services;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.exceptions.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    ResponseEntity<GenericResponseDto<UserDto>> signUp(CustomerUserSignupRequestDto customerSignRequestDto);

    ResponseEntity<GenericResponseDto<UserDto>> login(UserLoginRequestDto customerLoginRequestDto);

    ResponseEntity<GenericResponseDto<Void>> validateToken(String authHeader);

    ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(String authHeader);
}
