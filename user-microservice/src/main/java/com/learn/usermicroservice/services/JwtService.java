package com.learn.usermicroservice.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    Date extractExpiration(String token);

    Claims extractAllClaims(String token);

    String generateToken(Map<String, Object> claims);

    void validateToken(String token);

    UsernamePasswordAuthenticationToken getAuthentication(String token);
}
