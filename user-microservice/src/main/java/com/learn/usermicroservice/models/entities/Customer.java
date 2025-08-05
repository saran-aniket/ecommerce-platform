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
        ApplicationUser user = this.applicationUser;
        String email = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        Boolean isDeleted = user != null && user.isDeleted();
        Object registeredOn = (user != null && user.getRegisteredOn() != null) ? user.getRegisteredOn() : "";
        String firstName = (this.getFirstName() != null) ? this.getFirstName() : "";
        String lastName = (this.getLastName() != null) ? this.getLastName() : "";
        String phoneNumber = (this.getPhoneNumber() != null) ? this.getPhoneNumber() : "";
        List<String> authorities = (user != null && user.getUserRoles() != null)
                ? user.getUserRoles().stream().map(UserRole::getName).toList()
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
