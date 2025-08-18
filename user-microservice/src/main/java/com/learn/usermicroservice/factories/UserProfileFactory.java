package com.learn.usermicroservice.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.models.enums.UserRoleType;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class UserProfileFactory {

    private final ObjectMapper objectMapper;
    private UserRoleType userRoleType;

    public UserProfileFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setUserRoleType(String roleType) {
        this.userRoleType = UserRoleType.valueOf(roleType);
    }

    public String getUserProfileServiceBeanName() {
        if (userRoleType.equals(UserRoleType.ROLE_CUSTOMER)) {
            return "customerUserProfileService";
        } else if (userRoleType.equals(UserRoleType.ROLE_SELLER)) {
            return "sellerUserProfileService";
        } else if (userRoleType.equals(UserRoleType.ROLE_ADMIN)) {
            return "adminUserProfileService";
        } else {
            throw new IllegalArgumentException("Invalid user role type");
        }
    }

    public UserSignupRequestDto getConvertedUserSignupRequestDto(UserSignupRequestDto userSignupRequestDto) {
        if (userRoleType.equals(UserRoleType.ROLE_CUSTOMER)) {
            return objectMapper.convertValue(userSignupRequestDto, CustomerSignUpRequestDto.class);
        } else if (userRoleType.equals(UserRoleType.ROLE_SELLER)) {
            return objectMapper.convertValue(userSignupRequestDto, SellerSignUpRequestDto.class);
        } else {
            throw new IllegalArgumentException("Invalid user role type");
        }
    }

    public UserUpdateRequestDto getConvertedUserUpdateRequestDto(UserUpdateRequestDto userUpdateRequestDto) {
        if (userRoleType.equals(UserRoleType.ROLE_CUSTOMER)) {
            return objectMapper.convertValue(userUpdateRequestDto, CustomerUserUpdateRequestDto.class);
        } else if (userRoleType.equals(UserRoleType.ROLE_SELLER)) {
            return objectMapper.convertValue(userUpdateRequestDto, SellerUserUpdateRequestDto.class);
        } else {
            throw new IllegalArgumentException("Invalid user role type");
        }
    }

}
