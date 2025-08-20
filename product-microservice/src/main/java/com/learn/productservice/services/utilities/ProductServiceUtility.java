package com.learn.productservice.services.utilities;

import com.learn.productservice.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceUtility {

    private final JwtService jwtService;

    public ProductServiceUtility(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = jwtService.extractAllClaims(token);
        return claims.get("user_id", String.class);
    }
}
