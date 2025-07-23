package com.learn.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.apigateway.client.UserServiceClient;
import com.learn.apigateway.dtos.AuthenticationDto;
import com.learn.apigateway.dtos.GenericResponseDto;
import com.learn.apigateway.exceptions.GatewayAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class JwtAuthFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final UserServiceClient userServiceClient;


    public JwtAuthFilter(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public Function<ServerRequest, ServerRequest> validateToken(List<String> publicUrls) {
        return (request) -> {
            try{
                ServerRequest serverRequest = ServerRequest
                        .from(request)
                        .build();
                String path = serverRequest.path();
                if (!publicUrls.contains(path)) {
                    HttpHeaders requestHeaders = serverRequest.headers().asHttpHeaders();
                    if (!requestHeaders.containsKey("Authorization")) {
                        throw new GatewayAuthException("No Authorization Header");
                    }
                    String authHeader = requestHeaders.getFirst(HttpHeaders.AUTHORIZATION);
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        throw new GatewayAuthException("Invalid token");
                    }
                    ResponseEntity<GenericResponseDto<AuthenticationDto>> response = userServiceClient.getAuthentication(authHeader);
                    AuthenticationDto authenticationDto = response.getBody().getData();
                    log.info("Response: {}", authenticationDto);
                }
                log.info("Request from api-gateway: {}", request);
                log.info("Server Request from api-gateway: {}", serverRequest.toString());
//                log.info("Request body: {}", request.body(String.class));
                return request;

            }catch (Exception e){
                log.error("Exception: {}", e.getMessage(), e);
                throw new GatewayAuthException(e.getMessage());
            }
        };
    }
}
