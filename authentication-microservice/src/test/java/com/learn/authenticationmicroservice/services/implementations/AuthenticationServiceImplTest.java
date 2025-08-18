package com.learn.authenticationmicroservice.services.implementations;

import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.utilities.ASConstants;
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

        when(userServiceClient.signUp(ASConstants.CUSTOMER_ROLE, signupRequest)).thenReturn(expectedResponse);

        GenericResponseDto<UserDto> result = authenticationServiceImpl.signUp(ASConstants.CUSTOMER_ROLE, signupRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).signUp(ASConstants.CUSTOMER_ROLE, signupRequest);
    }

    @Test
    void login_returnsGenericResponseDto() {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        GenericResponseDto<UserDto> expectedResponse = new GenericResponseDto<>();

        when(userServiceClient.login(ASConstants.CUSTOMER_ROLE, loginRequest)).thenReturn(expectedResponse);

        GenericResponseDto<UserDto> result = authenticationServiceImpl.login(ASConstants.CUSTOMER_ROLE, loginRequest);

        assertSame(expectedResponse, result);
        verify(userServiceClient, times(1)).login(ASConstants.CUSTOMER_ROLE, loginRequest);
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
