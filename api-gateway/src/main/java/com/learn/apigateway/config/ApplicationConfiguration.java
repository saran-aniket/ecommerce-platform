package com.learn.apigateway.config;

import com.learn.apigateway.filter.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
    public RouterFunction<ServerResponse> serviceRouter(JwtAuthFilter jwtAuthFilter) {
        RouterFunction<ServerResponse> route = GatewayRouterFunctions.route("auth_route")
                .route(RequestPredicates.POST("/api/v1/authentication/users/**"),
                        HandlerFunctions.http())
                .route(RequestPredicates.GET("/api/v1/authentication/users/**"),
                        HandlerFunctions.http())
                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
                .before(BeforeFilterFunctions.uri("http://auth-service:4003"))
                .onError(Exception.class,
                        (throwable, serverRequest) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable.getMessage()))
                .onError(RuntimeException.class,
                        (throwable, serverRequest) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable.getMessage()))
                .build()
                .and(
                        GatewayRouterFunctions.route("user_route")
                                .route(RequestPredicates.POST("/api/v1/user/**"), HandlerFunctions.http())
                                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
                                .onError(AuthenticationException.class,
                                        (error, request) -> ServerResponse.badRequest().body(error.getMessage()))
                                .before(BeforeFilterFunctions.uri("http://user-service:4002"))
                                .build()
                ).and(
                        GatewayRouterFunctions.route("product_route")
                                .route(RequestPredicates.path("/api/v1/products/**"), HandlerFunctions.http())
                                .before(jwtAuthFilter.validateToken(PUBLIC_URLS))
                                .onError(AuthenticationException.class,
                                        (error, request) -> ServerResponse.badRequest().body(error.getMessage()))
                                .before(BeforeFilterFunctions.uri("http://product-service:4000"))
                                .build()
                );
        log.info("Auth Service Router Function: {}", route);
        return route;
    }

    @Bean
    public RouterFunction<ServerResponse> openApiRouter() {
        RouterFunction<ServerResponse> route = GatewayRouterFunctions.route("auth_service_openapi")
                .route(RequestPredicates.path("/auth-service/v3/api-docs/**")
                                .or(RequestPredicates.path("/auth-service/swagger-ui/**")),
                        HandlerFunctions.http())
                .before(BeforeFilterFunctions.rewritePath(
                        "/auth-service/v3/api-docs(?<path>/?.*)", "/v3/api-docs${path}"
                ))
                .before(BeforeFilterFunctions.rewritePath(
                        "/auth-service/swagger-ui(?<path>/?.*)", "/swagger-ui${path}"
                ))
                .before(BeforeFilterFunctions.uri("http://auth-service:4003"))
                .build()
                .and(
                        GatewayRouterFunctions.route("user_service_openapi")
                                .route(RequestPredicates.path("/user-service/v3/api-docs/**")
                                                .or(RequestPredicates.path("/user-service/swagger-ui/**")),
                                        HandlerFunctions.http())
                                .before(BeforeFilterFunctions.rewritePath(
                                        "/user-service/v3/api-docs(?<path>/?.*)", "/v3/api-docs${path}"
                                ))
                                .before(BeforeFilterFunctions.rewritePath(
                                        "/user-service/swagger-ui(?<path>/?.*)", "/swagger-ui${path}"
                                ))
                                .before(BeforeFilterFunctions.uri("http://user-service:4002"))
                                .build()
                ).and(
                        GatewayRouterFunctions.route("product_service_openapi")
                                .route(RequestPredicates.path("/product-service/v3/api-docs/**")
                                                .or(RequestPredicates.path("/product-service/swagger-ui/**")),
                                        HandlerFunctions.http())
                                .before(BeforeFilterFunctions.rewritePath(
                                        "/product-service/v3/api-docs(?<path>/?.*)", "/v3/api-docs${path}"
                                ))
                                .before(BeforeFilterFunctions.rewritePath(
                                        "/product-service/swagger-ui(?<path>/?.*)", "/swagger-ui${path}"
                                ))
                                .before(BeforeFilterFunctions.uri("http://product-service:4000"))
                                .build()
                );
        log.info("Open Api Doc Router Function: {}", route);
        return route;
    }
}
