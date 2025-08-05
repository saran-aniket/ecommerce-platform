package com.learn.usermicroservice.exceptions;

import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {
    private final String status;

    public CustomFeignException(String message, String status) {
        super(message);
        this.status = status;
    }
}
