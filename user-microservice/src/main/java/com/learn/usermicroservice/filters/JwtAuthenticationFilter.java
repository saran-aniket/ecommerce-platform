package com.learn.usermicroservice.filters;

import com.learn.usermicroservice.services.implementation.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private static final List<String> PUBLIC_URLS = List.of("/user/customer/login",
            "/user/customer/signup",
            "/user/customer/authenticate"
    );

    public JwtAuthenticationFilter(JwtServiceImpl jwtService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (PUBLIC_URLS.contains(path) || (request.getHeader("Authorization") == null || !request.getHeader(
                "Authorization").startsWith("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwtToken = request.getHeader("Authorization").substring(7);
            if (!jwtToken.isEmpty()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    SecurityContextHolder.getContext().setAuthentication(jwtService.getAuthentication(jwtToken));
                    log.info("Authentication is set in User Service");
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("Invalid Credentials");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
