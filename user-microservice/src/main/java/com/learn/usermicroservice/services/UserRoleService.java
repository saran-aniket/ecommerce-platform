package com.learn.usermicroservice.services;

import com.learn.usermicroservice.models.entities.UserRole;

import java.util.Optional;

public interface UserRoleService {
    UserRole createUserRole(UserRole userRole);

    Optional<UserRole> getUserRoleByName(String name);
}
