package com.ohbs.room.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ohbs.common.exception.ResourceNotFoundException;
import com.ohbs.room.dto.RoomRequestDTO;
import com.ohbs.room.dto.RoomResponseDTO;
import com.ohbs.room.model.Room;
import com.ohbs.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
//    private final HotelRepository hotelRepository;

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO dto) {
//        Hotel hotel = hotelRepository.findById(dto.getHotelId())
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
//
        Room room = Room.builder()
//                .hotel(hotel)
                .roomNumber(dto.getRoomNumber())
                .roomType(dto.getRoomType())
                .pricePerDay(dto.getPricePerDay())
                .description(dto.getDescription())
                .isAvailable(dto.getIsAvailable() != null && dto.getIsAvailable())
                .build();

        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public RoomResponseDTO getRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return mapToDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelIdAndIsAvailableTrue(hotelId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

//        Hotel hotel = hotelRepository.findById(dto.getHotelId())
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

//        room.setHotel(hotel);
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setPricePerDay(dto.getPricePerDay());
        room.setDescription(dto.getDescription());
//        room.setIsAvailable(dto.getIsAvailable() != null && dto.getIsAvailable());

        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    private RoomResponseDTO mapToDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .pricePerDay(room.getPricePerDay())
                .description(room.getDescription())
                .isAvailable(room.isAvailable())
//                .hotelId(room.getHotel().getId())
//                .hotelName(room.getHotel().getName())
                .build();
    }
}
