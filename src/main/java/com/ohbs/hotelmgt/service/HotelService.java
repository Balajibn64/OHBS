package com.ohbs.hotelmgt.service;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;

import java.util.List;

public interface HotelService {

    // Create a new hotel associated with a manager
    HotelResponseDTO createHotel(HotelRequestDTO dto, Long managerId);

    // Get a hotel by ID for a specific manager
    HotelResponseDTO getHotelById(long id, Long managerId);

    // Get all hotels associated with a specific manager
    List<HotelResponseDTO> getAllHotelsByManager(Long managerId);

    // Update a hotel by ID for a specific manager
    HotelResponseDTO updateHotel(Long id, HotelRequestDTO dto, Long managerId);

    // Soft-delete a hotel by ID for a specific manager
    void deleteHotel(Long id, Long managerId);
}
