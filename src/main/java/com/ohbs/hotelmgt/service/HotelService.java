package com.ohbs.hotelmgt.service;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.repository.HotelRepository;
import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotels);

    Hotel getHotelById(long id);

    List<Hotel> getAllHotels();

    Hotel updateHotel(Hotel hotels);
    void deleteHotel(Long id);

}

