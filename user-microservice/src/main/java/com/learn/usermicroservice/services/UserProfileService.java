package com.learn.usermicroservice.services;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.models.entities.ApplicationUser;

public interface UserProfileService {
    <T extends UserSignupRequestDto> ApplicationUser createUserProfile(T userSignupRequestDto, ApplicationUser applicationUser);

    <T extends UserUpdateRequestDto> ApplicationUser updateUserProfile(T userUpdateRequestDto, ApplicationUser applicationUser);

    ApplicationUser getUserProfileByApplicationUser(String email);

    ApplicationUser getUserProfileByUserId(String id);

}
