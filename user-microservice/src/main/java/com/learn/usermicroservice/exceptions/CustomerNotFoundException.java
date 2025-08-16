package com.learn.usermicroservice.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
