package com.ohbs.hotelmgt.controller;

import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.service.HotelService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> getHotels(@RequestParam(required = false) Long managerId) {
        if (managerId != null) {
            return ResponseEntity.ok(hotelService.getAllHotelsByManager(managerId));
        } else {
            return ResponseEntity.ok(hotelService.getAllHotels());
        }
    }
    
    @GetMapping("/hotels/manager/{managerId}")
    public ResponseEntity<Object> getHotelsByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(hotelService.findByManagerId(managerId));
    }

    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getHotelsCount(
        @RequestParam(value = "managerId", required = false) Long managerId) {

        long count = (managerId != null)
            ? hotelService.countByManager(managerId)
            : hotelService.countAll();

        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ResponseEntity.ok(result);
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
