package com.learn.authenticationmicroservice.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public GenericResponseDto<UserDto> signUp(String roleType, UserSignupRequestDto userSignupRequestDto) {
        try {
            String json = objectMapper.writeValueAsString(userSignupRequestDto);
            log.info("DTO as JSON from signup: {}", json);
        } catch (Exception e) {
            log.error("Error serializing DTO to JSON for logging", e);
        }
        GenericResponseDto<UserDto> genericResponseDto = userServiceClient.signUp(roleType, userSignupRequestDto);
        try {
            String responseJSON = objectMapper.writeValueAsString(genericResponseDto);
            log.info("Returning GenericResponseDto<CustomerDto>: {}", responseJSON);
        } catch (JsonProcessingException e) {
            log.error("Error serializing DTO to JSON for logging", e);
        }
        return genericResponseDto;
    }

    @Override
    public GenericResponseDto<UserDto> login(String roleType, UserLoginRequestDto userLoginRequestDto) {
        try {
            String json = objectMapper.writeValueAsString(userLoginRequestDto);
            log.info("DTO as JSON from login: {}", json);
        } catch (Exception e) {
            log.error("Error serializing DTO to JSON for logging", e);
        }
        return userServiceClient.login(roleType, userLoginRequestDto);
    }

    @Override
    public GenericResponseDto<AuthenticationDto> getAuthentication(String authHeader) {
        log.info("Authenticating user with token");
        log.info("***** getAuthentication: authHeader: {}", authHeader);
        return userServiceClient.getAuthentication(authHeader);
    }

    @Override
    public void logout(String authHeader) {
        userServiceClient.logout(authHeader);
    }
}
