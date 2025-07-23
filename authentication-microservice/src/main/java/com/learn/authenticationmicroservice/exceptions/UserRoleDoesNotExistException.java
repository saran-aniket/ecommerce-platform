package com.learn.authenticationmicroservice.exceptions;

public class UserRoleDoesNotExistException extends RuntimeException {
    public UserRoleDoesNotExistException(String message) {
        super(message);
    }
}
