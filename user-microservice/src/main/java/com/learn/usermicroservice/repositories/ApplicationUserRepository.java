package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findApplicationUserByEmailAndUserRoles(String email, List<UserRole> userRoles);

    Optional<ApplicationUser> findApplicationUserByEmail(String email);
}
