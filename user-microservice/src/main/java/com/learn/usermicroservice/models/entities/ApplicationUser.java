package com.learn.usermicroservice.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "application_users")
@Getter
@Setter
@Slf4j
@Inheritance(strategy = InheritanceType.JOINED)
public class ApplicationUser extends BaseModel {
    private String email;
    private String password;
    private boolean isDeleted;
    private boolean isActive;
    private Date registeredOn;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @OneToMany(mappedBy = "applicationUser")
    private Set<ApplicationUserRole> applicationUserRoles = new HashSet<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getApplicationUserRoles().stream().map(role -> new SimpleGrantedAuthority(role.getUserRole().getUserRoleType().name())).collect(Collectors.toList());
    }

    public Map<String, Object> getClaims() {
        String id = this.getId() != null ? this.getId().toString() : "";
        String email = this.getEmail() != null ? this.getEmail() : "";
        Boolean isDeleted = this.isDeleted();
        Object registeredOn = this.getRegisteredOn() != null ? this.getRegisteredOn() : "";
        String firstName = this.getFirstName() != null ? this.getFirstName() : "";
        String lastName = this.getLastName() != null ? this.getLastName() : "";
        String phoneNumber = this.getPhoneNumber() != null ? this.getPhoneNumber() : "";
        List<String> authorities = this.getApplicationUserRoles() != null
                ?
                this.getApplicationUserRoles().stream().map(role -> role.getUserRole().getUserRoleType().name()).toList()
                : java.util.Collections.emptyList();

        Map<String, Object> claimMap = Map.of(
                "user_id", id,
                "email", email,
                "isDeleted", isDeleted,
                "registeredOn", registeredOn,
                "firstName", firstName,
                "lastName", lastName,
                "phoneNumber", phoneNumber,
                "authorities", authorities
        );
        log.info("Claims: {}", claimMap);
        log.info("Authorities: {}", claimMap.get("authorities"));
        return claimMap;
    }
}
