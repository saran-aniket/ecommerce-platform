package com.learn.productservice.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtService {
    UsernamePasswordAuthenticationToken getAuthentication(String token);
}
