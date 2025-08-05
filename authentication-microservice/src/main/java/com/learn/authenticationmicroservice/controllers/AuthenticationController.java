package com.learn.authenticationmicroservice.controllers;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication/users")
@Slf4j
public class AuthenticationController {

//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

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

    @GetMapping("/authenticate")
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> authenticate(@RequestHeader("Authorization") String authHeader) {
        log.info("***** authenticate: authHeader: {}", authHeader);
        log.info("Authenticating user with token");
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.getAuthentication(authHeader));
    }

}
