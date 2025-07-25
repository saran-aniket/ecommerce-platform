package com.learn.apigateway.config;

import com.learn.apigateway.filter.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import javax.naming.AuthenticationException;
import java.util.List;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    private static final List<String> PUBLIC_URLS = List.of("/api/v1/authentication/users/login",
            "/api/v1/authentication/users/register",
            "/api/v1/authentication/users/authenticate"
    );

    @Bean
    public RouterFunction<ServerResponse> authServiceRouter(JwtAuthFilter jwtAuthFilter) {
        RouterFunction<ServerResponse> route = GatewayRouterFunctions.route("auth_route")
                .route(RequestPredicates.POST("/api/v1/authentication/users/**"),
                        HandlerFunctions.http())
                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
                .before((request) -> {
                    log.info("Request from api-gateway: {}", request);
                    return request;
                })
                .before(BeforeFilterFunctions.uri("http://auth-service:4003"))
                .onError(Exception.class,
                        (throwable, serverRequest) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable.getMessage()))
                .onError(RuntimeException.class,
                        (throwable, serverRequest) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable.getMessage()))
                .build();
//                .and(
//                        GatewayRouterFunctions.route("user_route")
//                                .route(RequestPredicates.POST("/user/customer/**"), HandlerFunctions.http())
//                                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
//                                .onError(AuthenticationException.class,
//                                        (error, request) -> ServerResponse.badRequest().body(error.getMessage()))
//                                .before(BeforeFilterFunctions.uri("http://user-service:4002"))
//                                .build()
//                );
        log.info("Auth Service Router Function: {}", route);
        return route;
    }

//    @Bean
//    public RouterFunction<ServerResponse> userServiceRouter(JwtAuthFilter jwtAuthFilter) {
//        RouterFunction<ServerResponse> route = GatewayRouterFunctions.route("user_route")
//                .route(RequestPredicates.path("/user/customer/**"), HandlerFunctions.http())
//                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
//                .onError(AuthenticationException.class,
//                        (error, request) -> ServerResponse.badRequest().body(error.getMessage()))
//                .before(BeforeFilterFunctions.uri("http://user-service:4002"))
//                .build();
//        log.info("User Service Router Function: {}", route);
//        return route;
//    }
}
