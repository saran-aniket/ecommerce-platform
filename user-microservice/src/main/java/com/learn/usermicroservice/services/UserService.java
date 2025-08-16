package com.learn.usermicroservice.services;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.models.entities.ApplicationUser;

import java.util.List;

public interface UserService {
    ApplicationUser createUser(UserSignupRequestDto userSignupRequestDto, String roleType);

    ApplicationUser updateUser(UserUpdateRequestDto userUpdateRequestDto, String roleType);

    void deleteUser(String email, String roleType);

    ApplicationUser getUserByEmail(String email);

    ApplicationUser getUserByIdAndRoleType(String id, String roleType);

    List<ApplicationUser> getAllUsers();

}
