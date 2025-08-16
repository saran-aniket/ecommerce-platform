package com.learn.productservice.exceptions;

public class DuplicateProductFoundException extends RuntimeException {
    public DuplicateProductFoundException(String exception) {
        super(exception);
    }
}
