package com.learn.authenticationmicroservice.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String unauthorized) {
        super(unauthorized);
    }
}
