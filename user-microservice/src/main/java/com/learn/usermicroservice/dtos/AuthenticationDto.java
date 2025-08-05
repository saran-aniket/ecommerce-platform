package com.learn.usermicroservice.dtos;

import com.learn.usermicroservice.models.enums.TokenClaims;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AuthenticationDto {
    private String email;
    private Boolean isDeleted;
    private Date registeredOn;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> authorities;

    @SuppressWarnings("unchecked")
    public static AuthenticationDto from(Claims claims) {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setEmail(claims.get(TokenClaims.USER_EMAIL.getClaimName(), String.class));
        authenticationDto.setFirstName(claims.get(TokenClaims.USER_FIRST_NAME.getClaimName(), String.class));
        authenticationDto.setLastName(claims.get(TokenClaims.USER_LAST_NAME.getClaimName(), String.class));
        authenticationDto.setPhoneNumber(claims.get(TokenClaims.USER_PHONE_NUMBER.getClaimName(), String.class));
        authenticationDto.setIsDeleted(claims.get(TokenClaims.USER_IS_DELETED.getClaimName(), Boolean.class));
        authenticationDto.setRegisteredOn(claims.get(TokenClaims.USER_REGISTERED_ON.getClaimName(), Date.class));
        authenticationDto.setAuthorities(claims.get(TokenClaims.USER_AUTHORITIES.getClaimName(), List.class));
        return authenticationDto;
    }
}
