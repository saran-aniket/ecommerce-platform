package com.learn.usermicroservice.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Entity(name = "customers")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends ApplicationUser {
    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

}
