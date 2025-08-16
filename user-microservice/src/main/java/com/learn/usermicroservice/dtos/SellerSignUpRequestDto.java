package com.learn.usermicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerSignUpRequestDto extends UserSignupRequestDto {
    private String companyName;
}
