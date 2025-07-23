package com.learn.authenticationmicroservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUserSignupRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

//    public static ApplicationUser ApplicationUserfrom(CustomerUserSignupRequestDto customerUserSignupRequestDto){
//        ApplicationUser applicationUser = new ApplicationUser();
//        applicationUser.setEmail(customerUserSignupRequestDto.getEmail());
//        applicationUser.setPassword(customerUserSignupRequestDto.getPassword());
//        return applicationUser;
//    }
}
