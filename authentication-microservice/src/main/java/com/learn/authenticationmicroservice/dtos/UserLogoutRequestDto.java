package com.learn.authenticationmicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogoutRequestDto {
    private String email;
}
