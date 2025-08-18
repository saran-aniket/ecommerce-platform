package com.learn.productservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.util.Date;
import java.util.UUID;


@MappedSuperclass
public class BaseModel {
    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", length = 36, columnDefinition = "char(36)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
