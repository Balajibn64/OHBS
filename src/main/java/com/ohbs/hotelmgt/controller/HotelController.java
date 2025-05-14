package com.ohbs.hotelmgt.controller;

import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.service.HotelService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hotels") // Base URL: http://localhost:8080/hotels
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotels) {
        Hotel createdHotel = hotelService.createHotel(hotels);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }   
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@PathVariable("id") Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        HotelResponseDTO dto = HotelResponseDTO.fromEntity(hotel);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        List<HotelResponseDTO> dtos = hotels.stream()
                .map(HotelResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable("id") Long id, @RequestBody Hotel hotels) {
        hotels.setId(id);
        return ResponseEntity.ok().body(hotelService.updateHotel(hotels));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable("id") Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok().body("Hotel deleted successfully.");
    }
}
