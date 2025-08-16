package com.learn.productservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category_id"}))
@Getter
@Setter
public class Product extends BaseModel {
    private String name;
    @Column(length = 1000)
    private String description;
    private float price;
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Long external_id;
}
