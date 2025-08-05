package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtServiceImpl implements JwtService {

    private final String SECRET;

    private final Long EXPIRATION;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration) {
        this.SECRET = secret;
        this.EXPIRATION = expiration;
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public void validateToken(String token) {
        Claims claims = extractAllClaims(token);

        log.info("Token Claims: {}", claims.toString());

        if (isTokenExpired(token)) {
            throw new JwtException("Token is expired");
        }
        log.info("Token is valid");
    }

    @Override
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .add(claims)
                .and()
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = extractAllClaims(token);
        Collection<GrantedAuthority> authorities;
        if (claims.get("authorities") != null) {
            authorities =
                    ((List<String>) claims.get("authorities")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            log.info("Authorities: {}", authorities);
        } else {
            throw new JwtException("Authorities are not present in the token");
        }
        return new UsernamePasswordAuthenticationToken(token, null, authorities);
    }
}
