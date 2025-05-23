package com.ohbs.hotelmgt.repository;

import com.ohbs.hotelmgt.model.Hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByName(String name);

    List<Hotel> findByIsDeletedFalse();

    Optional<Hotel> findByIdAndIsDeletedFalse(Long id);

    // ✅ Add this method to fetch hotels of a specific manager (used for authorization and filtering)
    List<Hotel> findByManagerIdAndIsDeletedFalse(Long managerId);
    @Query("SELECT h FROM Hotel h JOIN h.rooms r WHERE h.location = :location AND h.isDeleted = false GROUP BY h.id ORDER BY MIN(r.pricePerDay) ASC")
    List<Hotel> findByLocationOrderByPriceAsc(String location);

    @Query("SELECT h FROM Hotel h JOIN h.rooms r WHERE h.location = :location AND h.isDeleted = false GROUP BY h.id ORDER BY MIN(r.pricePerDay) DESC")
    List<Hotel> findByLocationOrderByPriceDesc(String location);
}