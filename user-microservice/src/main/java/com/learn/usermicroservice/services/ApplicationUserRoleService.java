package com.learn.usermicroservice.services;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;

import java.util.List;

public interface ApplicationUserRoleService {
    List<ApplicationUser> getAllApplicationUsersByRoleType(UserRoleType roleType);

    ApplicationUserRole saveApplicationUserRole(ApplicationUser applicationUser, UserRole userRole, boolean isActive);
}
