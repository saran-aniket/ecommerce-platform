package com.learn.apigateway.filter;

import com.learn.apigateway.client.UserServiceClient;
import com.learn.apigateway.dtos.AuthenticationDto;
import com.learn.apigateway.dtos.GenericResponseDto;
import com.learn.apigateway.dtos.ResponseStatus;
import com.learn.apigateway.exceptions.GatewayAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private UserServiceClient userServiceClient;
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        userServiceClient = mock(UserServiceClient.class);
        jwtAuthFilter = new JwtAuthFilter(userServiceClient);
    }

    @Test
    void validateToken_publicUrl_allowsRequest() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/api/v1/authentication/users/login");
        when(request.headers()).thenReturn(mock(ServerRequest.Headers.class));
        when(request.headers().asHttpHeaders()).thenReturn(mock(HttpHeaders.class));
        when(request.headers().asHttpHeaders().getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        List<String> publicUrls = List.of("/api/v1/authentication/users/login");

        Function<ServerRequest, ServerRequest> validator = jwtAuthFilter.validateToken(publicUrls);

        ServerRequest result = validator.apply(request);

        assertSame(request, result);
        verifyNoInteractions(userServiceClient);
    }

    @Test
    void validateToken_missingAuthorizationHeader_throwsException() {
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/secured/resource");
        when(request.headers()).thenReturn(headers);

        // no "Authorization" header
        HttpHeaders httpHeaders = new HttpHeaders();
        when(headers.asHttpHeaders()).thenReturn(httpHeaders);

        List<String> publicUrls = Collections.emptyList();

        Function<ServerRequest, ServerRequest> validator = jwtAuthFilter.validateToken(publicUrls);

        GatewayAuthException exception = assertThrows(
                GatewayAuthException.class,
                () -> validator.apply(request)
        );
        assertEquals("No Authorization Header", exception.getMessage());
    }

    @Test
    void validateToken_invalidAuthorizationHeader_throwsException() {
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/secured/resource");
        when(request.headers()).thenReturn(headers);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "InvalidToken");
        when(headers.asHttpHeaders()).thenReturn(httpHeaders);

        List<String> publicUrls = Collections.emptyList();

        Function<ServerRequest, ServerRequest> validator = jwtAuthFilter.validateToken(publicUrls);

        GatewayAuthException exception = assertThrows(
                GatewayAuthException.class,
                () -> validator.apply(request)
        );
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void validateToken_validToken_callsUserServiceClient() {
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/secured/resource");
        when(request.headers()).thenReturn(headers);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer good.token");
        when(headers.asHttpHeaders()).thenReturn(httpHeaders);

        GenericResponseDto<AuthenticationDto> genericResponse = new GenericResponseDto<>();
        AuthenticationDto authDto = new AuthenticationDto();
        genericResponse.setData(authDto);
        genericResponse.setStatus(ResponseStatus.SUCCESS);

        when(userServiceClient.getAuthentication("Bearer good.token"))
                .thenReturn(genericResponse);

        List<String> publicUrls = Collections.emptyList();

        Function<ServerRequest, ServerRequest> validator = jwtAuthFilter.validateToken(publicUrls);

        ServerRequest result = validator.apply(request);

        assertSame(request, result);
        verify(userServiceClient).getAuthentication("Bearer good.token");
    }
}