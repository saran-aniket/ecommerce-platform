package com.learn.authenticationmicroservice.client;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.exceptions.UnauthorizedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://user-service:4002", path="/user/customer")
public interface UserServiceClient {
    @PostMapping("/signup")
    ResponseEntity<GenericResponseDto<UserDto>> signUp(@RequestBody CustomerUserSignupRequestDto customerSignRequestDto);


    @PostMapping("/login")
    ResponseEntity<GenericResponseDto<UserDto>> login(@RequestBody UserLoginRequestDto customerLoginRequestDto);

//    @PostMapping("/logout")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
//    ResponseEntity<GenericResponseDto<Void>> logout(@RequestHeader("Authorization") String authHeader);

    @PostMapping("/validate-token")
    ResponseEntity<GenericResponseDto<Void>> validateToken(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/authenticate")
    ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(@RequestHeader("Authorization") String authHeader) throws UnauthorizedException;
}
