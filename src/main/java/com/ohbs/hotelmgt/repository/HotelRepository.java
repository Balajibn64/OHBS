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
    
    Optional<Hotel> findByManagerIdAndIsDeletedFalse(Long id);

    //method to fetch hotels of a specific manager (used for authorization and filtering)
    @Query("""
    	    SELECT h FROM Hotel h 
    	    JOIN h.rooms r 
    	    WHERE h.location = :location 
    	      AND h.isDeleted = false 
    	      AND (:minRating IS NULL OR h.rating >= :minRating)
    	      AND (:maxRating IS NULL OR h.rating <= :maxRating)
    	    GROUP BY h.id 
    	    ORDER BY MIN(r.pricePerDay) ASC
    	""")
    	List<Hotel> findByLocationAndRatingRangeOrderByPriceAsc(String location, Double minRating, Double maxRating);

    	@Query("""
    	    SELECT h FROM Hotel h 
    	    JOIN h.rooms r 
    	    WHERE h.location = :location 
    	      AND h.isDeleted = false 
    	      AND (:minRating IS NULL OR h.rating >= :minRating)
    	      AND (:maxRating IS NULL OR h.rating <= :maxRating)
    	    GROUP BY h.id 
    	    ORDER BY MIN(r.pricePerDay) DESC
    	""")
    	List<Hotel> findByLocationAndRatingRangeOrderByPriceDesc(String location, Double minRating, Double maxRating);

}