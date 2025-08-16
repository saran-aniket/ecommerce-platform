package com.learn.usermicroservice.services;

import com.learn.usermicroservice.models.entities.UserRole;

public interface UserRoleService {
    UserRole createUserRole(UserRole userRole);

    UserRole getUserRoleByName(String name);
}
