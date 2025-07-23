package com.learn.usermicroservice.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String unauthorized) {
        super(unauthorized);
    }
}
