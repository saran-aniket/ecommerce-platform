package com.learn.productservice.exceptions;

public class DuplicateCategoryFoundException extends RuntimeException{
    public DuplicateCategoryFoundException(String exception) {
        super(exception);
    }
}
