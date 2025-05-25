package com.ohbs.hotelmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohbs.hotelmgt.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByName(String name);

    List<Hotel> findByManagerIdAndIsDeletedFalse(Long managerId);

    List<Hotel> findByIsDeletedFalse();

    java.util.Optional<Hotel> findByIdAndIsDeletedFalse(Long id);

    long countByManagerIdAndIsDeletedFalse(Long managerId);

    long countByIsDeletedFalse();
}
