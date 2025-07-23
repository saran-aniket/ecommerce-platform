package com.learn.productservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "sellers")
@Entity
public class Seller extends BaseModel {
    private String name;
    private String email;
    private String phone;
}
