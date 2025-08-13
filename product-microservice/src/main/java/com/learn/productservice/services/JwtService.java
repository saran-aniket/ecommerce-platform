package com.learn.productservice.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtService {
    public UsernamePasswordAuthenticationToken getAuthentication(String token);
}
