package com.learn.authenticationmicroservice.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException(String invalidCredentials) {
        super(invalidCredentials);
    }
}
