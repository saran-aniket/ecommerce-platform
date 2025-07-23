package com.learn.usermicroservice.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException(String invalidCredentials) {
        super(invalidCredentials);
    }
}
