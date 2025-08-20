package com.learn.authenticationmicroservice.client;

import com.learn.authenticationmicroservice.configs.CustomFeignClientConfiguration;
import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.exceptions.UnauthorizedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://user-service:4002", path = "/api/v1/user", configuration = CustomFeignClientConfiguration.class)
public interface UserServiceClient {
    @PostMapping("/auth/signup")
    GenericResponseDto<UserDto> signUp(@RequestParam("roleType") String roleType,
                                       @RequestBody UserSignupRequestDto userSignupRequestDto);


    @PostMapping("/auth/login")
    GenericResponseDto<UserDto> login(@RequestParam("roleType") String roleType,
                                      @RequestBody UserLoginRequestDto userLoginRequestDto);

    @PostMapping("/auth/logout")
    GenericResponseDto<Void> logout(@RequestHeader("Authorization") String authHeader);

    @PostMapping("/auth/validate-token")
    GenericResponseDto<Void> validateToken(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/auth")
    GenericResponseDto<AuthenticationDto> getAuthentication(@RequestHeader("Authorization") String authHeader) throws UnauthorizedException;
}
