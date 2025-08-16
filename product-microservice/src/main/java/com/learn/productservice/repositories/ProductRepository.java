package com.learn.productservice.repositories;

import com.learn.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> getProductById(UUID id);

    Optional<Product> getProductByNameAndCategory_Name(String name, String categoryName);
}
