package com.ohbs.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ohbs.common.model.Role;
import com.ohbs.common.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);

//    List<User> findAllByRole(String role);
    List<User> findAllByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
