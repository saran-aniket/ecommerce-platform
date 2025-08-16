package com.learn.usermicroservice.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "application_user_roles", // you can name the join table as you wish
            joinColumns = @JoinColumn(name = "application_user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserRole> userRoles;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getUserRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Map<String, Object> getClaims() {
        String email = this.getEmail() != null ? this.getEmail() : "";
        Boolean isDeleted = this.isDeleted();
        Object registeredOn = this.getRegisteredOn() != null ? this.getRegisteredOn() : "";
        String firstName = this.getFirstName() != null ? this.getFirstName() : "";
        String lastName = this.getLastName() != null ? this.getLastName() : "";
        String phoneNumber = this.getPhoneNumber() != null ? this.getPhoneNumber() : "";
        List<String> authorities = this.getUserRoles() != null
                ? this.getUserRoles().stream().map(UserRole::getName).toList()
                : java.util.Collections.emptyList();

        Map<String, Object> claimMap = Map.of(
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
