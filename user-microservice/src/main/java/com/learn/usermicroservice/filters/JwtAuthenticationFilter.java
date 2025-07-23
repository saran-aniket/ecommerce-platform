package com.learn.usermicroservice.filters;

import com.learn.usermicroservice.services.CustomUserDetailService;
import com.learn.usermicroservice.services.implementation.JwtServiceImpl;
import com.learn.usermicroservice.services.implementation.TokenBlackListServiceImpl;
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

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final CustomUserDetailService customUserDetailService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final TokenBlackListServiceImpl tokenBlackListService;

    public JwtAuthenticationFilter(JwtServiceImpl jwtService, CustomUserDetailService customUserDetailService, HandlerExceptionResolver handlerExceptionResolver, TokenBlackListServiceImpl tokenBlackListService) {
        this.jwtService = jwtService;
        this.customUserDetailService = customUserDetailService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")){
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
            }else{
                throw new AuthenticationCredentialsNotFoundException("Invalid Credentials");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
