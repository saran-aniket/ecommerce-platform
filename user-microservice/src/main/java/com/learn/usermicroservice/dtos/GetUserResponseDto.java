package com.learn.usermicroservice.dtos;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.models.entities.Seller;
import com.learn.usermicroservice.utilities.USConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String companyName;
    private String userRoleType;

    public static GetUserResponseDto from(ApplicationUser applicationUser) {
        GetUserResponseDto getUserResponseDto = new GetUserResponseDto();
        getUserResponseDto.setEmail(applicationUser.getEmail());
        getUserResponseDto.setFirstName(applicationUser.getFirstName());
        getUserResponseDto.setLastName(applicationUser.getLastName());
        if (applicationUser instanceof Customer) {
            getUserResponseDto.setUserRoleType(USConstants.CUSTOMER_ROLE);
        } else if (applicationUser instanceof Seller) {
            getUserResponseDto.setUserRoleType(USConstants.SELLER_ROLE);
            getUserResponseDto.setCompanyName(((Seller) applicationUser).getCompanyName());
        }
        return getUserResponseDto;
    }
}
