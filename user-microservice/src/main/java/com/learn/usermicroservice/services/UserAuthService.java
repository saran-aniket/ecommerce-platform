package com.learn.usermicroservice.services;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.models.Token;

public interface UserAuthService {
    Token login(String email, String password, String roleType) throws InvalidCredentialException;

    void logout(String token);

    void validateToken(String token);

    AuthenticationDto getAuthentication(String authorizationHeader);
}
