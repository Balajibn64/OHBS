package com.ohbs.manager.repository;

import com.ohbs.manager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	Optional<Manager> findByUserIdAndIsDeletedFalse(Long userId); // ✅ VALID
    List<Manager> findAllByIsDeletedFalse();
}