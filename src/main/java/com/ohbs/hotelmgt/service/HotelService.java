package com.ohbs.hotelmgt.service;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;

import java.util.List;

import com.ohbs.hotelmgt.dto.HotelFilterDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface HotelService {

    // Get hotels by filter criteria (location, rating, etc.)
    List<HotelResponseDTO> getHotelByFilter(HotelFilterDTO dto);

    // Create a new hotel associated with a manager (get manager from token in request)
    HotelResponseDTO createHotel(HotelRequestDTO dto, HttpServletRequest request);

    // Get a hotel by ID for a specific manager (get manager from token in request)
    HotelResponseDTO getHotelById(long id, HttpServletRequest request);

    // Get all hotels associated with a specific manager (get manager from token in request)
    List<HotelResponseDTO> getAllHotelsByManager(HttpServletRequest request);

    // Update a hotel by ID for a specific manager (get manager from token in request)
    HotelResponseDTO updateHotel(Long id, HotelRequestDTO dto, HttpServletRequest request);

    // Soft-delete a hotel by ID for a specific manager (get manager from token in request)
    void deleteHotel(Long id, HttpServletRequest request);
}
