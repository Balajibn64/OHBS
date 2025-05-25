package com.ohbs.Customer.repository;

import com.ohbs.Customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find a customer by userId where the profile is not soft-deleted
    Optional<Customer> findByUserIdAndIsDeletedFalse(Long userId);

    // Find a customer by userId regardless of the deleted flag (include soft-deleted)
    Optional<Customer> findByUserId(Long userId);
    
    Optional<Customer> findByIdAndIsDeletedFalse(Long id);

    
}
