package com.learn.usermicroservice.services.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TokenBlackListServiceImplTest {

    private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.payload";
    private static final Long FUTURE_TIME = System.currentTimeMillis() + 10000;
    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    JwtServiceImpl jwtServiceImpl;
    @InjectMocks
    private TokenBlackListServiceImpl tokenBlackListServiceImpl;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTokenToBlackList_shouldSetKeyInRedisWithExpiration() {
        when(jwtServiceImpl.extractExpiration(TEST_TOKEN)).thenReturn(new Date(FUTURE_TIME));

        tokenBlackListServiceImpl.addTokenToBlackList(TEST_TOKEN);

        verify(redisTemplate.opsForValue()).set(eq("blacklist:" + TEST_TOKEN), eq("blacklisted"),
                longThat(arg -> arg <= 10000 && arg > 0), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void isTokenBlackListed_shouldReturnTrue_whenKeyPresent() {
        when(redisTemplate.hasKey("blacklist:" + TEST_TOKEN)).thenReturn(true);

        Boolean result = tokenBlackListServiceImpl.isTokenBlackListed(TEST_TOKEN);

        assertTrue(result);
        verify(redisTemplate).hasKey("blacklist:" + TEST_TOKEN);

    }

    @Test
    void isTokenBlackListed_shouldReturnTrue_whenKeyNotPresent() {
        when(redisTemplate.hasKey("blacklist:" + TEST_TOKEN)).thenReturn(false);

        Boolean result = tokenBlackListServiceImpl.isTokenBlackListed(TEST_TOKEN);

        assertFalse(result);
        verify(redisTemplate).hasKey("blacklist:" + TEST_TOKEN);

    }
}