package com.ohbs.hotelmgt.controller;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.service.HotelService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.ohbs.hotelmgt.dto.HotelFilterDTO;


@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("path")
    public ResponseEntity<List<HotelResponseDTO>> getHotelByFilter(@RequestBody HotelFilterDTO dto) {
        return ResponseEntity.ok(hotelService.getHotelByFilter(dto));
    }
    

    // POST /hotels
    @PostMapping
    public ResponseEntity<HotelResponseDTO> createHotel(
            HttpServletRequest request,
            @Valid @RequestBody HotelRequestDTO dto) {
        HotelResponseDTO createdHotel = hotelService.createHotel(dto, request);
        return ResponseEntity.status(201).body(createdHotel);
    }

    // GET /hotels/{id}
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(
            @PathVariable Long id,
            HttpServletRequest request) {
        HotelResponseDTO hotel = hotelService.getHotelById(id, request);
        return ResponseEntity.ok(hotel);
    }

    // GET /hotels
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotelsByManager(
            HttpServletRequest request) {
        List<HotelResponseDTO> hotels = hotelService.getAllHotelsByManager(request);
        return ResponseEntity.ok(hotels);
    }

    // PUT /hotels/{id}
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(
            @PathVariable Long id,
            HttpServletRequest request,
            @Valid @RequestBody HotelRequestDTO dto) {
        HotelResponseDTO updatedHotel = hotelService.updateHotel(id, dto, request);
        return ResponseEntity.ok(updatedHotel);
    }

    // DELETE /hotels/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(
            @PathVariable Long id,
            HttpServletRequest request) {
        hotelService.deleteHotel(id, request);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }
}
