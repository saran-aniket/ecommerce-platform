package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.services.TokenBlackListService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtServiceImpl jwtService;

    public TokenBlackListServiceImpl(RedisTemplate<String, Object> redisTemplate, JwtServiceImpl jwtService) {
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtService;
    }

    public void addTokenToBlackList(String token) {
        redisTemplate.opsForValue().set("blacklist:" + token, "blacklisted", jwtService.extractExpiration(token).getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public Boolean isTokenBlackListed(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}
