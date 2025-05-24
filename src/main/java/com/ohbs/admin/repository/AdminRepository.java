package com.ohbs.admin.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ohbs.admin.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUserId(Long userId);
    
    
    @Query("SELECT a FROM Admin a WHERE a.firstName LIKE %:keyword% OR a.lastName LIKE %:keyword%")
    Optional<Admin> searchByName(@Param("keyword") String keyword);
}