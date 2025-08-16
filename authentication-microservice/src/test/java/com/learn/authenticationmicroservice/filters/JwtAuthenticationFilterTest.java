package com.learn.authenticationmicroservice.filters;

import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.AuthenticationDto;
import com.learn.authenticationmicroservice.dtos.GenericResponseDto;
import com.learn.authenticationmicroservice.dtos.ResponseStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterChain mockFilterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipAuthenticationForPublicUrls() throws Exception {
        when(mockRequest.getServletPath()).thenReturn("/api/v1/authentication/users/login");

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateWhenValidTokenProvided() throws Exception {
        String token = "jwt.token";
        when(mockRequest.getServletPath()).thenReturn("/api/secured");
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        AuthenticationDto authDto = new AuthenticationDto();
        authDto.setAuthorities(List.of("CUSTOMER", "ADMIN"));
        GenericResponseDto<AuthenticationDto> responseDto = new GenericResponseDto<>();
        responseDto.setStatus(ResponseStatus.SUCCESS);
        responseDto.setData(authDto);

        when(userServiceClient.getAuthentication(token)).thenReturn(responseDto);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(token, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> "CUSTOMER".equals(a.getAuthority())));
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void shouldNotAuthenticateWhenAuthorizationHeaderIsMissing() throws Exception {
        when(mockRequest.getServletPath()).thenReturn("/api/protected");
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        String token = "";
        when(mockRequest.getServletPath()).thenReturn("/api/protected");
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(handlerExceptionResolver).resolveException(eq(mockRequest), eq(mockResponse), isNull(), any(AuthenticationCredentialsNotFoundException.class));
    }

    @Test
    void shouldCallHandlerExceptionResolverOnUserServiceException() throws Exception {
        String token = "jwt.token";
        when(mockRequest.getServletPath()).thenReturn("/api/protected");
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(userServiceClient.getAuthentication(token)).thenThrow(new RuntimeException("Service error"));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(handlerExceptionResolver).resolveException(eq(mockRequest), eq(mockResponse), isNull(), any(RuntimeException.class));
    }
}