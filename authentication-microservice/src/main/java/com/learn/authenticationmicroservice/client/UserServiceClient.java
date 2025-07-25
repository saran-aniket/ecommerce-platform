package com.learn.authenticationmicroservice.client;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.exceptions.UnauthorizedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://user-service:4002", path = "/user/customer")
public interface UserServiceClient {
    @PostMapping("/signup")
    GenericResponseDto<CustomerDto> signUp(@RequestBody CustomerUserSignupRequestDto customerSignRequestDto);


    @PostMapping("/login")
    GenericResponseDto<CustomerDto> login(@RequestBody UserLoginRequestDto customerLoginRequestDto);

//    @PostMapping("/logout")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
//    GenericResponseDto<Void>> logout(@RequestHeader("Authorization") String authHeader);

    @PostMapping("/validate-token")
    GenericResponseDto<Void> validateToken(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/authenticate")
    GenericResponseDto<AuthenticationDto> getAuthentication(@RequestHeader("Authorization") String authHeader) throws UnauthorizedException;
}
