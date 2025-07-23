package com.learn.usermicroservice.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "user_roles")
@Getter
@Setter
public class UserRole extends BaseModel {
    private String name;
    @ManyToMany(mappedBy = "userRoles")
    private List<ApplicationUser> applicationUsers;
}
