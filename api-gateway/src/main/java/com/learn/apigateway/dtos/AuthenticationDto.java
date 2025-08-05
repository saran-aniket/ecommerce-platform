package com.learn.apigateway.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AuthenticationDto {
    private String email;
    private Boolean isDeleted;
    private Date registeredOn;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> authorities;
}
