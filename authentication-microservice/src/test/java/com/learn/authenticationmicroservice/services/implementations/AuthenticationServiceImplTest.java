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

    @Test
    void signUp_returnsGenericResponseDto() {
        UserSignupRequestDto signupRequest = new UserSignupRequestDto();
        GenericResponseDto<UserDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.signUp("CUSTOMER", signupRequest)).thenReturn(expectedResponse);

        GenericResponseDto<UserDto> result = authenticationServiceImpl.signUp("CUSTOMER", signupRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).signUp("CUSTOMER", signupRequest);
    }

    @Test
    void login_returnsGenericResponseDto() {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        GenericResponseDto<UserDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.login("CUSTOMER", loginRequest)).thenReturn(expectedResponse);

        GenericResponseDto<UserDto> result = authenticationServiceImpl.login("CUSTOMER", loginRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).login("CUSTOMER", loginRequest);
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
