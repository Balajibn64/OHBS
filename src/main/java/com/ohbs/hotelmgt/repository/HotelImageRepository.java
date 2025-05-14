package com.ohbs.hotelmgt.repository;

import com.ohbs.hotelmgt.model.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {
    List<HotelImage> findByHotelId(Long hotelId);
    @Query("SELECT i FROM HotelImage i WHERE i.hotel.id = :hotelId AND i.hotel.isDeleted = false")
    List<HotelImage> findActiveImagesByHotelId(@Param("hotelId") Long hotelId);

}
