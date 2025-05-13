package com.ohbs.hotelmgt.repository;

import com.ohbs.hotelmgt.model.Hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Custom query method to find a hotels by its name
    Hotel findByName(String name);
    
    List<Hotel> findByIsDeletedFalse();

    Optional<Hotel> findByIdAndIsDeletedFalse(Long id);

}
