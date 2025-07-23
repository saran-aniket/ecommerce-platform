package com.learn.apigateway.exceptions;

public class GatewayAuthException extends RuntimeException {
    public GatewayAuthException(String message) {
        super(message);
    }
}
