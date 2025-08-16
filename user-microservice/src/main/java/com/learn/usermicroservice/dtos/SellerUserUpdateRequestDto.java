package com.learn.usermicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerUserUpdateRequestDto extends UserUpdateRequestDto {
    private String companyName;
}
