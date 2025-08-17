package com.learn.apigateway.client;


import com.learn.apigateway.dtos.AuthenticationDto;
import com.learn.apigateway.dtos.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-microservice", url = "http://user-service:4002", path = "/api/v1/user")
public interface UserServiceClient {

    @GetMapping("/auth")
    GenericResponseDto<AuthenticationDto> getAuthentication(@RequestHeader("Authorization") String authHeader);
}
