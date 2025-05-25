package com.ohbs.hotelmgt.service;

import java.util.List;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.model.Hotel;

public interface HotelService {

    HotelResponseDTO createHotel(HotelRequestDTO dto, Long managerId);

    HotelResponseDTO getHotelById(long id, Long managerId);

    List<HotelResponseDTO> getAllHotelsByManager(Long managerId);

    List<HotelResponseDTO> getAllHotels();

    Hotel updateHotel(Hotel updatedHotel);

    HotelResponseDTO updateHotel(Long id, HotelRequestDTO dto, Long managerId);

    void deleteHotel(Long id, Long managerId);

    long countByManager(Long managerId);

    long countAll();

	Object findByManagerId(Long managerId);
}
