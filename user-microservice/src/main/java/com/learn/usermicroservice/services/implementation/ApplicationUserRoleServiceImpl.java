package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.exceptions.UserNotFoundException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.ApplicationUserRoleId;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRoleRepository;
import com.learn.usermicroservice.services.ApplicationUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationUserRoleServiceImpl implements ApplicationUserRoleService {
    private final ApplicationUserRoleRepository applicationUserRoleRepository;

    public ApplicationUserRoleServiceImpl(ApplicationUserRoleRepository applicationUserRoleRepository) {
        this.applicationUserRoleRepository = applicationUserRoleRepository;
    }

    @Override
    public List<ApplicationUser> getAllApplicationUsersByRoleType(UserRoleType roleType) {
        Optional<List<ApplicationUserRole>> optionalApplicationUserRoleList =
                applicationUserRoleRepository.findApplicationUserRolesByUserRole_UserRoleType(roleType);
        if (optionalApplicationUserRoleList.isEmpty()) {
            throw new UserNotFoundException("No Application User Roles found for role type " + roleType.name());
        }
        return optionalApplicationUserRoleList.get().stream().map(ApplicationUserRole::getApplicationUser).collect(Collectors.toSet()).stream().toList();
    }

    @Override
    public ApplicationUserRole saveApplicationUserRole(ApplicationUser applicationUser, UserRole userRole, boolean isActive) {
        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(applicationUser);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setIsActive(isActive);

        ApplicationUserRoleId applicationUserRoleId = new ApplicationUserRoleId();
        applicationUserRoleId.setApplicationUserId(applicationUser.getId());
        applicationUserRoleId.setUserRoleId(userRole.getId());
        applicationUserRole.setApplicationUserRoleId(applicationUserRoleId);

        return applicationUserRoleRepository.save(applicationUserRole);
    }
}
