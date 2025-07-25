package com.learn.authenticationmicroservice.controllers;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GenericResponseDto<CustomerDto>> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        ResponseEntity<GenericResponseDto<CustomerDto>> responseEntity =
                ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(userLoginRequestDto));
        log.info("Returning GenericResponseDto<UserDto>: {}", responseEntity);
        return responseEntity;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponseDto<CustomerDto>> register(@RequestBody CustomerUserSignupRequestDto customerSignupRequestDto) {
        log.info("***** register: customerSignupRequestDto: {}", customerSignupRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signUp(customerSignupRequestDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> authenticate(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.getAuthentication(authHeader));
    }

}
