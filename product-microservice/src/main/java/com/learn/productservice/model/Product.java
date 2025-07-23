package com.learn.productservice.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "products")
public class Product extends BaseModel{
    private String name;
    @Column(length = 1000)
    private String description;
    private float price;
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Long external_id;

    public Long getExternal_id() {
        return external_id;
    }

    public void setExternal_id(Long external_id) {
        this.external_id = external_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
