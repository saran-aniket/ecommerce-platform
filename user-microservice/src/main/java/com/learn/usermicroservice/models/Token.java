package com.learn.usermicroservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class Token {
    private String accessToken;
    private Long expiresAt;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }


    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}
