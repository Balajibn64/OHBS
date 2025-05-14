package com.ohbs.Customer.repository;

import com.ohbs.Customer.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserIdAndIsDeletedFalse(Long userId);
}