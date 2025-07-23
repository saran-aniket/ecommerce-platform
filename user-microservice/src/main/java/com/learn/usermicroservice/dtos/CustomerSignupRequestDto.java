package com.learn.usermicroservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSignupRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
}
