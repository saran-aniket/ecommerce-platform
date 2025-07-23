package com.learn.authenticationmicroservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String token;
    private String email;

    public static UserDto UserDtoFrom(String token, String email){
        UserDto userDto = new UserDto();
        userDto.setToken(token);
        userDto.setEmail(email);
        return userDto;
    }
}
