package com.learn.usermicroservice.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "application_user_roles")
@Getter
@Setter
public class ApplicationUserRole {
    @EmbeddedId
    private ApplicationUserRoleId applicationUserRoleId;

    @ManyToOne
    @MapsId("applicationUserId")
    @JoinColumn(name = "application_user_id")
    private ApplicationUser applicationUser;

    @ManyToOne
    @MapsId("userRoleId")
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

    private Boolean isActive;

    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;
}
