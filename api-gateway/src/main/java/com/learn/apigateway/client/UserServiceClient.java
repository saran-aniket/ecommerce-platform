package com.learn.apigateway.client;


import com.learn.apigateway.dtos.AuthenticationDto;
import com.learn.apigateway.dtos.UserDto;
import com.learn.apigateway.dtos.CustomerSignupRequestDto;
import com.learn.apigateway.dtos.GenericResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-microservice", url = "http://user-service:4002", path = "/user/customer")
public interface UserServiceClient {
    @PostMapping("/signup")
    ResponseEntity<GenericResponseDto<UserDto>> signUp(@RequestBody CustomerSignupRequestDto customerSignupRequestDto);

    @PostMapping("/validate-token")
    ResponseEntity<GenericResponseDto<UserDto>> validateToken(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/authenticate")
    ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(@RequestHeader("Authorization") String authHeader);
}
