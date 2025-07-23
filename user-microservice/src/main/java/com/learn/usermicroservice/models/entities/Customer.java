package com.learn.usermicroservice.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Entity(name = "customers")
@Getter
@Setter
public class Customer extends BaseModel {
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id")
    private ApplicationUser applicationUser;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

    public Map<String, Object> getClaims() {
        Map<String, Object> claimMap = Map.of(
                "email", this.applicationUser.getEmail(),
                "isDeleted", this.applicationUser.isDeleted(),
                "registeredOn", this.applicationUser.getRegisteredOn(),
                "firstName", this.getFirstName(),
                "lastName", this.getLastName(),
                "phoneNumber", this.getPhoneNumber(),
                "authorities", this.applicationUser.getUserRoles().stream().map(UserRole::getName).collect(java.util.stream.Collectors.toList())
        );
        log.info("Claims: {}", claimMap);
        log.info("Authorities: {}", claimMap.get("authorities"));
        return claimMap;
    }

}
