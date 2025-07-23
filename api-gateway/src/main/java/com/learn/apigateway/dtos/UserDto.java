package com.learn.apigateway.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String token;
    private String email;
}
