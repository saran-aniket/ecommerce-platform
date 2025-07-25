package com.learn.authenticationmicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    private String token;
    private String email;
}
