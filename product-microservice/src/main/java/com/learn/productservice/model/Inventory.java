package com.learn.productservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "inventories")
@Entity
@Getter
@Setter
public class Inventory extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private UUID seller_id;
    private int quantity;
}
