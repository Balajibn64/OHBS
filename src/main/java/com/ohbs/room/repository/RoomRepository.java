package com.ohbs.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ohbs.room.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelIdAndIsAvailableTrue(Long hotelId);

    Optional<Room> findByHotelIdAndRoomNumber(Long hotelId, String roomNumber);

    boolean existsByRoomNumberAndHotelId(String roomNumber, Long hotelId);
}
