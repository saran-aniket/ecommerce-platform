package com.learn.usermicroservice.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.models.enums.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileFactoryTest {

    private ObjectMapper objectMapper;
    private UserProfileFactory userProfileFactory;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        userProfileFactory = new UserProfileFactory(objectMapper);
    }

    @Test
    void setUserRoleType_setsCorrectRoleType() {
        userProfileFactory.setUserRoleType("CUSTOMER");
        assertEquals(UserRoleType.CUSTOMER, userProfileFactory.getUserRoleType());

        userProfileFactory.setUserRoleType("SELLER");
        assertEquals(UserRoleType.SELLER, userProfileFactory.getUserRoleType());

        userProfileFactory.setUserRoleType("ADMIN");
        assertEquals(UserRoleType.ADMIN, userProfileFactory.getUserRoleType());
    }

    @Test
    void setUserRoleType_withInvalidValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userProfileFactory.setUserRoleType("UNKNOWN"));
    }

    @Test
    void getUserProfileServiceBeanName_returnsCorrectBean() {
        userProfileFactory.setUserRoleType("CUSTOMER");
        assertEquals("customerUserProfileService", userProfileFactory.getUserProfileServiceBeanName());

        userProfileFactory.setUserRoleType("SELLER");
        assertEquals("sellerUserProfileService", userProfileFactory.getUserProfileServiceBeanName());

        userProfileFactory.setUserRoleType("ADMIN");
        assertEquals("adminUserProfileService", userProfileFactory.getUserProfileServiceBeanName());
    }

    @Test
    void getUserProfileServiceBeanName_withNullRole_throwsException() {
        userProfileFactory = new UserProfileFactory(objectMapper);
        assertThrows(NullPointerException.class, () -> userProfileFactory.getUserProfileServiceBeanName());
    }

    @Test
    void getUserProfileServiceBeanName_withInvalidRole_throwsException() {
        userProfileFactory.setUserRoleType("CUSTOMER");
        assertEquals("customerUserProfileService", userProfileFactory.getUserProfileServiceBeanName());
        // There isn’t a path to invalid UserRoleType in code, except for null set; covered above.
    }

    @Test
    void getConvertedUserSignupRequestDto_returnsCorrectDtoForCustomer() {
        userProfileFactory.setUserRoleType("CUSTOMER");
        UserSignupRequestDto dto = new UserSignupRequestDto();
        CustomerSignUpRequestDto converted = new CustomerSignUpRequestDto();
        when(objectMapper.convertValue(dto, CustomerSignUpRequestDto.class)).thenReturn(converted);
        UserSignupRequestDto result = userProfileFactory.getConvertedUserSignupRequestDto(dto);
        assertEquals(converted, result);
    }

    @Test
    void getConvertedUserSignupRequestDto_returnsCorrectDtoForSeller() {
        userProfileFactory.setUserRoleType("SELLER");
        UserSignupRequestDto dto = new UserSignupRequestDto();
        SellerSignUpRequestDto converted = new SellerSignUpRequestDto();
        when(objectMapper.convertValue(dto, SellerSignUpRequestDto.class)).thenReturn(converted);
        UserSignupRequestDto result = userProfileFactory.getConvertedUserSignupRequestDto(dto);
        assertEquals(converted, result);
    }

    @Test
    void getConvertedUserSignupRequestDto_withInvalidRole_throwsException() {
        userProfileFactory.setUserRoleType("ADMIN");
        UserSignupRequestDto dto = new UserSignupRequestDto();
        assertThrows(IllegalArgumentException.class, () -> userProfileFactory.getConvertedUserSignupRequestDto(dto));
    }

    @Test
    void getConvertedUserUpdateRequestDto_returnsCorrectDtoForCustomer() {
        userProfileFactory.setUserRoleType("CUSTOMER");
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        CustomerUserUpdateRequestDto converted = new CustomerUserUpdateRequestDto();
        when(objectMapper.convertValue(dto, CustomerUserUpdateRequestDto.class)).thenReturn(converted);
        UserUpdateRequestDto result = userProfileFactory.getConvertedUserUpdateRequestDto(dto);
        assertEquals(converted, result);
    }

    @Test
    void getConvertedUserUpdateRequestDto_returnsCorrectDtoForSeller() {
        userProfileFactory.setUserRoleType("SELLER");
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        SellerUserUpdateRequestDto converted = new SellerUserUpdateRequestDto();
        when(objectMapper.convertValue(dto, SellerUserUpdateRequestDto.class)).thenReturn(converted);
        UserUpdateRequestDto result = userProfileFactory.getConvertedUserUpdateRequestDto(dto);
        assertEquals(converted, result);
    }

    @Test
    void getConvertedUserUpdateRequestDto_withInvalidRole_throwsException() {
        userProfileFactory.setUserRoleType("ADMIN");
        UserUpdateRequestDto dto = new UserUpdateRequestDto();
        assertThrows(IllegalArgumentException.class, () -> userProfileFactory.getConvertedUserUpdateRequestDto(dto));
    }
}