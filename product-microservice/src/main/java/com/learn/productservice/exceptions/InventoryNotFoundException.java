package com.learn.productservice.exceptions;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String s) {
        super(s);
    }
}
