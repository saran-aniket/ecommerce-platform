package com.learn.authenticationmicroservice.services.implementations;

import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @BeforeEach
    void setUp() {
    }

    @Test
    void signUp_returnsGenericResponseDto() {
        CustomerUserSignupRequestDto signupRequest = new CustomerUserSignupRequestDto();
        GenericResponseDto<CustomerDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.signUp(signupRequest)).thenReturn(expectedResponse);

        GenericResponseDto<CustomerDto> result = authenticationServiceImpl.signUp(signupRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).signUp(signupRequest);
    }

    @Test
    void login_returnsGenericResponseDto() {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        GenericResponseDto<CustomerDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.login(loginRequest)).thenReturn(expectedResponse);

        GenericResponseDto<CustomerDto> result = authenticationServiceImpl.login(loginRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).login(loginRequest);
    }

    @Test
    void getAuthentication_returnsGenericResponseDto() {
        String authHeader = "Bearer test-token";
        GenericResponseDto<AuthenticationDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.getAuthentication(authHeader)).thenReturn(expectedResponse);

        GenericResponseDto<AuthenticationDto> result = authenticationServiceImpl.getAuthentication(authHeader);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).getAuthentication(authHeader);
    }
}
