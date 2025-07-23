package com.learn.authenticationmicroservice.controllers;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication/users")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseDto<UserDto>> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        ResponseEntity<GenericResponseDto<UserDto>> responseEntity = authenticationService.login(userLoginRequestDto);
        log.info("Returning GenericResponseDto<UserDto>: {}", responseEntity);
        return responseEntity;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponseDto<UserDto>> register(@RequestBody CustomerUserSignupRequestDto customerSignupRequestDto) {
        return authenticationService.signUp(customerSignupRequestDto);
    }

    @PostMapping("/auth")
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> authenticate(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.getAuthentication(authHeader);
    }

}
