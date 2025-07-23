package com.learn.authenticationmicroservice.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String invalidToken) {
        super(invalidToken);
    }
}
