package com.learn.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SellerApplicationUserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
