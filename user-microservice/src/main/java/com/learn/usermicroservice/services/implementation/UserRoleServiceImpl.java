package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import com.learn.usermicroservice.services.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public Optional<UserRole> getUserRoleByName(String name) {
        return userRoleRepository.findUserRoleByUserRoleType(UserRoleType.valueOf(name));
    }
}
