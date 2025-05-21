package com.ohbs.room.service;

import java.util.List;

import com.ohbs.room.dto.RoomRequestDTO;
import com.ohbs.room.dto.RoomResponseDTO;

public interface RoomService {
    RoomResponseDTO createRoom(RoomRequestDTO dto);
    RoomResponseDTO getRoom(Long id);
    List<RoomResponseDTO> getRoomsByHotel(Long hotelId);
    RoomResponseDTO updateRoom(Long id, RoomRequestDTO dto);
    void deleteRoom(Long id);
}
