package com.ohbs.hotelmgt.repository;

import com.ohbs.hotelmgt.model.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {
    List<HotelImage> findByHotelId(Long hotelId);
}
