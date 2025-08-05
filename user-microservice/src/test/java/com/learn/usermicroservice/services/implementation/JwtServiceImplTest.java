package com.learn.usermicroservice.services.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {
    private static final String SECRET = Base64.getEncoder().encodeToString("01234567890123456789012345678901".getBytes());
    private static final Long EXPIRATION = 1000 * 60 * 5L; // 5 minutes
    private JwtServiceImpl jwtServiceImpl;
    private String validToken;
    private Map<String, Object> testClaims;


    @BeforeEach
    void setUp() {
        jwtServiceImpl = new JwtServiceImpl(SECRET, EXPIRATION);
        testClaims = new HashMap<>();
        testClaims.put("sub", "testuser");
        testClaims.put("authorities", Arrays.asList("ROLE_CUSTOMER", "ROLE_ADMIN"));
        validToken = jwtServiceImpl.generateToken(testClaims);
    }

    @Test
    void extractExpiration_shouldReturnExpirationDate() {
        Date expiration = jwtServiceImpl.extractExpiration(validToken);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractAllClaims_shouldReturnClaims() {
        Claims claims = jwtServiceImpl.extractAllClaims(validToken);
        assertEquals("testuser", claims.get("sub"));
        assertEquals(Arrays.asList("ROLE_CUSTOMER", "ROLE_ADMIN"), claims.get("authorities"));
    }

    @Test
    void validateToken_shouldSucceed_WhenTokenValid() {
        assertDoesNotThrow(() -> jwtServiceImpl.validateToken(validToken));
    }

    @Test
    void validateToken_shouldThrow_WhenTokenExpired() throws InterruptedException {
        JwtServiceImpl shortTokenService = new JwtServiceImpl(SECRET, 1L);
        String expiredToken = shortTokenService.generateToken(testClaims);
        Thread.sleep(2);
        assertThrows(JwtException.class, () -> shortTokenService.validateToken(expiredToken));
    }

    @Test
    void generateToken_shouldContainClaims() {
        Map<String, Object> claimsMap = new HashMap<>(testClaims);
        String token = jwtServiceImpl.generateToken(claimsMap);
        Claims claims = jwtServiceImpl.extractAllClaims(token);
        assertEquals("testuser", claims.get("sub"));
        assertEquals(Arrays.asList("ROLE_CUSTOMER", "ROLE_ADMIN"), claims.get("authorities"));
    }

    @Test
    void getAuthentication_shouldReturnAuthTokenWithAuthorities() {
        UsernamePasswordAuthenticationToken auth =
                jwtServiceImpl.getAuthentication(validToken);
        assertEquals(validToken, auth.getPrincipal());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void getAuthentication_shouldThrow_WhenAuthoritiesMissing() {
        Map<String, Object> claimsNoAuth = new HashMap<>();
        claimsNoAuth.put("sub", "nouser");
        String token = jwtServiceImpl.generateToken(claimsNoAuth);
        assertThrows(JwtException.class, () -> jwtServiceImpl.getAuthentication(token));
    }

}