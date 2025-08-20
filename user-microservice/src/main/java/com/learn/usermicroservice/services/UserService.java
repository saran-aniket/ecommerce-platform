package com.learn.usermicroservice.services;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ApplicationUser createUser(UserSignupRequestDto userSignupRequestDto, String roleType);

    ApplicationUser updateUser(UserUpdateRequestDto userUpdateRequestDto, String roleType);

    void deleteUser(String email, String roleType);

    ApplicationUser getUserByEmail(String email);

    ApplicationUser getUserByIdAndRoleType(String id, String roleType);

    List<ApplicationUser> getAllActiveUsersByRoleType(String roleType);

    Optional<ApplicationUserRole> getUserByRoleTypeAndEmail(String email, UserRoleType roleType);

}
