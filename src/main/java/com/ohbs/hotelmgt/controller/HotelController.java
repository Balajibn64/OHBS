package com.ohbs.hotelmgt.controller;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.service.HotelService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // POST /hotels?managerId=1
    @PostMapping
    public ResponseEntity<HotelResponseDTO> createHotel(
            @RequestParam Long managerId,
            @Valid @RequestBody HotelRequestDTO dto) {
        HotelResponseDTO createdHotel = hotelService.createHotel(dto, managerId);
        return ResponseEntity.status(201).body(createdHotel);
    }

    // GET /hotels/{id}?managerId=1
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(
            @PathVariable Long id,
            @RequestParam Long managerId) {
        HotelResponseDTO hotel = hotelService.getHotelById(id, managerId);
        return ResponseEntity.ok(hotel);
    }

    // GET /hotels?managerId=1
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotelsByManager(
            @RequestParam Long managerId) {
        List<HotelResponseDTO> hotels = hotelService.getAllHotelsByManager(managerId);
        return ResponseEntity.ok(hotels);
    }

    // PUT /hotels/{id}?managerId=1
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(
            @PathVariable Long id,
            @RequestParam Long managerId,
            @Valid @RequestBody HotelRequestDTO dto) {
        HotelResponseDTO updatedHotel = hotelService.updateHotel(id, dto, managerId);
        return ResponseEntity.ok(updatedHotel);
    }

    // DELETE /hotels/{id}?managerId=1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(
            @PathVariable Long id,
            @RequestParam Long managerId) {
        hotelService.deleteHotel(id, managerId);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }
}
