package com.learn.authenticationmicroservice.controllers;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import com.learn.authenticationmicroservice.utilities.ASConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void login() throws Exception {
        GenericResponseDto<UserDto> responseDto = new GenericResponseDto<>();
        when(authenticationService.login(eq(ASConstants.CUSTOMER_ROLE), any(UserLoginRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/authentication/users/login")
                        .param("roleType", ASConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void register() throws Exception {
        GenericResponseDto<UserDto> responseDto = new GenericResponseDto<>();
        when(authenticationService.signUp(eq(ASConstants.CUSTOMER_ROLE), any(UserSignupRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/authentication/users/register")
                        .param("roleType", ASConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void authenticate() throws Exception {
        GenericResponseDto<AuthenticationDto> responseDto = new GenericResponseDto<>();
        when(authenticationService.getAuthentication(any(String.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/authentication/users/authenticate")
                        .header("Authorization", "Bearer sometoken"))
                .andExpect(status().isOk());
    }
}