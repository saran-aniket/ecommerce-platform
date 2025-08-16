package com.learn.usermicroservice.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "sellers")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "seller_id")
public class Seller extends ApplicationUser {
    private String companyName;
    @OneToOne(mappedBy = "seller")
    private Address address;
}
