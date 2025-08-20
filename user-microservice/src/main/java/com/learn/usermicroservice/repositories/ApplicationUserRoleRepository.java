package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.ApplicationUserRoleId;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationUserRoleRepository extends JpaRepository<ApplicationUserRole, ApplicationUserRoleId> {
    Optional<ApplicationUserRole> findApplicationUserRolesByApplicationUserAndUserRole(ApplicationUser applicationUser, UserRole userRole);

    Optional<List<ApplicationUserRole>> findApplicationUserRolesByUserRole_UserRoleType(UserRoleType userRoleUserRoleType);
}
