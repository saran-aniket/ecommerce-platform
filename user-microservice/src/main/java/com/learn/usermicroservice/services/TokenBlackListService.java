package com.learn.usermicroservice.services;

public interface TokenBlackListService {
    void addTokenToBlackList(String token);

    Boolean isTokenBlackListed(String token);
}
