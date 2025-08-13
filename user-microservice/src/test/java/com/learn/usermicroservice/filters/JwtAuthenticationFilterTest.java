package com.learn.usermicroservice.filters;

import com.learn.usermicroservice.services.implementation.JwtServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterChain mockFilterChain;

    @Mock
    private HandlerExceptionResolver mockHandlerExceptionResolver;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        // Clean up SecurityContext before each test
        SecurityContextHolder.clearContext();

        when(mockRequest.getServletPath()).thenReturn("/api/test");
    }

    @Test
    void doFilterInternal_shouldAuthenticateWhenValidTokenProvided() throws ServletException, IOException {
        String jwtToken = "valid.jwt.token";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(jwtService.getAuthentication(jwtToken)).thenReturn(authenticationToken);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        SecurityContext context = SecurityContextHolder.getContext();
        assertEquals(authenticationToken, context.getAuthentication());
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWhenNoTokenProvided() throws ServletException, IOException {
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        String jwtToken = "invalid.jwt.token";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.getAuthentication(jwtToken)).thenThrow(JwtException.class);


        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
//        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void doFilterInternal_shouldSkipNonBearerToken() throws ServletException, IOException {
        when(mockRequest.getHeader("Authorization")).thenReturn("Basic xyz");

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }
}