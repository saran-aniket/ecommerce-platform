package com.learn.authenticationmicroservice.exceptions;

import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {
    private final String status;

    public CustomFeignException(String message, String status) {
        super(message);
        this.status = status;
    }
}
