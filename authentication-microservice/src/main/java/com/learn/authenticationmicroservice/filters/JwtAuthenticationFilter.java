package com.learn.authenticationmicroservice.filters;

import com.learn.authenticationmicroservice.client.UserServiceClient;
import com.learn.authenticationmicroservice.dtos.AuthenticationDto;
import com.learn.authenticationmicroservice.dtos.GenericResponseDto;
import com.learn.authenticationmicroservice.dtos.ResponseStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserServiceClient userServiceClient;

    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver,  UserServiceClient userServiceClient) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwtToken = request.getHeader("Authorization").substring(7);
            if (!jwtToken.isEmpty()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    GenericResponseDto<AuthenticationDto> authenticationResponse = userServiceClient.getAuthentication(jwtToken);
                    if(authenticationResponse != null && authenticationResponse.getStatus() == ResponseStatus.SUCCESS){
                        Collection<GrantedAuthority> authorityCollection = authenticationResponse.getData().getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtToken, null, authorityCollection);
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        log.info("Authentication is set in Auth Service");
                    }else{
                        throw new AuthenticationCredentialsNotFoundException("Invalid Credentials");
                    }
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
