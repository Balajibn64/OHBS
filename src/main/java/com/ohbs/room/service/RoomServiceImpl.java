package com.ohbs.room.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ohbs.common.exception.ResourceNotFoundException;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.repository.HotelRepository;
import com.ohbs.room.dto.RoomRequestDTO;
import com.ohbs.room.dto.RoomResponseDTO;
import com.ohbs.room.exception.InvalidRoomDataException;
import com.ohbs.room.exception.RoomAlreadyExistsException;
import com.ohbs.room.exception.RoomNotAvailableException;
import com.ohbs.room.model.Room;
import com.ohbs.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO dto) {
        // Validate hotel
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + dto.getHotelId()));

        // Check for duplicate room number in the same hotel
        if (roomRepository.existsByRoomNumberAndHotelId(dto.getRoomNumber(), dto.getHotelId())) {
            throw new RoomAlreadyExistsException("Room number '" + dto.getRoomNumber() + "' already exists in this hotel.");
        }

        // Additional business rule (though DTO should handle it)
        if (dto.getPricePerDay() < 0) {
            throw new InvalidRoomDataException("Price per day cannot be negative.");
        }

        Room room = Room.builder()
                .hotel(hotel)
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
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return mapToDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getRoomsByHotel(Long hotelId) {
        // Check hotel existence
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }

        return roomRepository.findByHotelIdAndIsAvailableTrue(hotelId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + dto.getHotelId()));

        // Optional: check if updating to a duplicate room number
        if (!room.getRoomNumber().equals(dto.getRoomNumber()) &&
                roomRepository.existsByRoomNumberAndHotelId(dto.getRoomNumber(), dto.getHotelId())) {
            throw new RoomAlreadyExistsException("Room number '" + dto.getRoomNumber() + "' already exists in this hotel.");
        }

        room.setHotel(hotel);
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setPricePerDay(dto.getPricePerDay());
        room.setDescription(dto.getDescription());
        room.setAvailable(dto.getIsAvailable() != null && dto.getIsAvailable());

        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Optional: Prevent deletion if room is not available
        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Cannot delete room because it is currently marked as unavailable.");
        }

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
                .hotelId(room.getHotel().getId())
                .hotelName(room.getHotel().getName())
                .build();
    }
}
