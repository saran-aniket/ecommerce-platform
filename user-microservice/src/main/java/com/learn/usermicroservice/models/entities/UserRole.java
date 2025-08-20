package com.learn.usermicroservice.models.entities;

import com.learn.usermicroservice.models.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "user_roles")
@Getter
@Setter
public class UserRole extends BaseModel {
    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;
    @OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL)
    private Set<ApplicationUserRole> applicationUserRoles = new HashSet<>();
}
