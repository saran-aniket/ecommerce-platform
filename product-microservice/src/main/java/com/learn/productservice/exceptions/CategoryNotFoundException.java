package com.learn.productservice.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String exception) {
        super(exception);
    }
}
