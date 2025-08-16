package com.learn.usermicroservice.exceptions;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(String sellerNotFoundForEmail) {
        super(sellerNotFoundForEmail);
    }
}
