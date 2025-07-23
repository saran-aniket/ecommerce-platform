package com.learn.authenticationmicroservice.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final UserServiceClient userServiceClient;


    public AuthenticationServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public ResponseEntity<GenericResponseDto<UserDto>> signUp(CustomerUserSignupRequestDto customerSignRequestDto) {
        try {
            String json = objectMapper.writeValueAsString(customerSignRequestDto);
            log.info("DTO as JSON from signup: {}", json);
        } catch (Exception e) {
            log.error("Error serializing DTO to JSON for logging", e);
        }

        return userServiceClient.signUp(customerSignRequestDto);
    }

    @Override
    public ResponseEntity<GenericResponseDto<UserDto>> login(UserLoginRequestDto customerLoginRequestDto) {
        try {
            String json = objectMapper.writeValueAsString(customerLoginRequestDto);
            log.info("DTO as JSON from login: {}", json);
        } catch (Exception e) {
            log.error("Error serializing DTO to JSON for logging", e);
        }
        return userServiceClient.login(customerLoginRequestDto);
    }

    @Override
    public ResponseEntity<GenericResponseDto<Void>> validateToken(String authHeader) {
        return userServiceClient.validateToken(authHeader);
    }

    @Override
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(String authHeader) {
        return userServiceClient.getAuthentication(authHeader);
    }
}
