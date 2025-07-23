package com.learn.usermicroservice.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String invalidToken) {
        super(invalidToken);
    }
}
