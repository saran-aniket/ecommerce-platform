package com.learn.productservice.services.implementations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private static final String SECRET = Base64.getEncoder().encodeToString("01234567890123456789012345678901".getBytes());
    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(SECRET);
    }

    @Test
    void extractAllClaims_validToken_returnsClaims() {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", "user1");
        String token = Jwts.builder()
                .claims()
                .add(claimsMap)
                .and()
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .compact();

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("user1", claims.get("sub"));
    }

    @Test
    void getAuthentication_withAuthorities_returnsAuthToken() {
        List<String> authorities = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("authorities", authorities);

        String token = Jwts.builder()
                .claims()
                .add(claimsMap)
                .and()
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .compact();

        UsernamePasswordAuthenticationToken auth = jwtService.getAuthentication(token);

        assertEquals(token, auth.getPrincipal());
        List<String> fetchedAuthorities = new ArrayList<>();
        for (GrantedAuthority a : auth.getAuthorities()) {
            fetchedAuthorities.add(a.getAuthority());
        }
        assertTrue(fetchedAuthorities.containsAll(authorities));
    }

    @Test
    void getAuthentication_missingAuthorities_throwsJwtException() {
        Map<String, Object> claimsMap = new HashMap<>();
        // No authorities field included
        String token = Jwts.builder()
                .claims()
                .add(claimsMap)
                .and()
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .compact();

        assertThrows(JwtException.class, () -> jwtService.getAuthentication(token));
    }
}