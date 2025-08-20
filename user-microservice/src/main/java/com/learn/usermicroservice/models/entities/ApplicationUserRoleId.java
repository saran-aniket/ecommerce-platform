package com.learn.usermicroservice.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Setter
@Getter
public class ApplicationUserRoleId implements Serializable {
    @Column(name = "application_user_id")
    private UUID applicationUserId;

    @Column(name = "user_role_id")
    private UUID userRoleId;
}
