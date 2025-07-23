package com.learn.usermicroservice.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenClaims {
    USER_EMAIL("email"),
    USER_IS_DELETED("isDeleted"),
    USER_REGISTERED_ON("registeredOn"),
    USER_FIRST_NAME("firstName"),
    USER_LAST_NAME("lastName"),
    USER_PHONE_NUMBER("phoneNumber"),
    USER_AUTHORITIES("authorities"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp"),
    ALGORITHM("alg"),
    TYP("typ");

    private final String claimName;
}
