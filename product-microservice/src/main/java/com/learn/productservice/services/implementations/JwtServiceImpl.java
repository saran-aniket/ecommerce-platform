package com.learn.productservice.services.implementations;

import com.learn.productservice.services.JwtService;
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
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final String SECRET;

    private final Long EXPIRATION;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration) {
        this.SECRET = secret;
        this.EXPIRATION = expiration;
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

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
