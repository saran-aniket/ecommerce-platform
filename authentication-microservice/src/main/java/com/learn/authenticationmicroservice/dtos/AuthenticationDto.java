package com.learn.authenticationmicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticationDto {
    private String email;
    private Boolean isDeleted;
    private String registeredOn;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> authorities;
}
