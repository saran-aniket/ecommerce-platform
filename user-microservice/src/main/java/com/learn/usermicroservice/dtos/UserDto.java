package com.learn.usermicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String token;
    private String email;
}
