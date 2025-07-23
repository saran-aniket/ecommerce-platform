package com.learn.usermicroservice.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
