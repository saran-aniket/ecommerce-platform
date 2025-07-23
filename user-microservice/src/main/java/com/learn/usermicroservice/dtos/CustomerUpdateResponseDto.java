package com.learn.usermicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
