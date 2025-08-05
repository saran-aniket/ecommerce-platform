package com.learn.usermicroservice.services.implementation;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthEntryPointJwtImplTest {

    private AuthEntryPointJwtImpl authEntryPointJwt;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private AuthenticationException mockAuthException;
    private ByteArrayOutputStream byteStream;


    @BeforeEach
    void setUp() throws IOException {
        authEntryPointJwt = new AuthEntryPointJwtImpl();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockAuthException = mock(AuthenticationException.class);
        // use a simple ByteArrayOutputStream for assertion
        byteStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            @Override
            public void write(int b) {
                byteStream.write(b);
            }
        };

        when(mockResponse.getOutputStream()).thenReturn(servletOutputStream);
        when(mockRequest.getServletPath()).thenReturn("/api/test");

    }

    @Test
    void commence_shouldSetUnauthorizedAndWriteErrorMessage() throws IOException, ServletException {
        when(mockAuthException.getMessage()).thenReturn("Unauthorized");

        authEntryPointJwt.commence(mockRequest, mockResponse, mockAuthException);

        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockResponse).setContentType("application/json");
        String responseBody = byteStream.toString();
        assert responseBody.contains("\"message\":\"Unauthorized\"");

    }

    @Test
    void commence_shouldHandleNullExceptionMessage() throws IOException, ServletException {
        when(mockAuthException.getMessage()).thenReturn(null);

        authEntryPointJwt.commence(mockRequest, mockResponse, mockAuthException);

        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockResponse).setContentType("application/json");
        String responseBody = byteStream.toString();
        assert responseBody.contains("\"message\":null");
    }
}