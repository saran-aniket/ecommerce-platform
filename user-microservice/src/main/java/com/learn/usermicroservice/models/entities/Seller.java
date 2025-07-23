package com.learn.usermicroservice.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "sellers")
@Getter
@Setter
public class Seller extends BaseModel {
    private String companyName;
    @OneToOne(mappedBy = "seller")
    private Address address;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id")
    private ApplicationUser applicationUser;
}
