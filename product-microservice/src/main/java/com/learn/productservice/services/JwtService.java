package com.learn.productservice.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtService {

    Claims extractAllClaims(String token);

    UsernamePasswordAuthenticationToken getAuthentication(String token);
}
