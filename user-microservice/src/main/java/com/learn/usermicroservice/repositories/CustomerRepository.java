package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findCustomerById(UUID uuid);

    Optional<Customer> findCustomerByEmail(String email);
}
