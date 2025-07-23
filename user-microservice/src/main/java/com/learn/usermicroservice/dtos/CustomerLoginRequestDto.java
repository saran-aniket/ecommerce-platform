package com.learn.usermicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerLoginRequestDto {
    private String email;
    private String password;
}
