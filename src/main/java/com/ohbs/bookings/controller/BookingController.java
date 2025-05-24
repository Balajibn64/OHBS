package com.ohbs.bookings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.dto.BookingResponseDto;
import com.ohbs.bookings.service.BookingService;
import com.ohbs.common.exception.UnauthorizedException;
import com.ohbs.security.jwt.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Create a new booking")
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @Operation(summary = "Get booking by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @Operation(summary = "Get all bookings for a customer")
    @GetMapping("/customer")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCustomer(HttpServletRequest request) {
        Long customerId = extractUserId(request);
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    @Operation(summary = "Get all bookings for a room")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(bookingService.getBookingsByRoom(roomId));
    }

    @Operation(summary = "Update an existing booking")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id, @RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(bookingService.updateBooking(id, dto));
    }

    @Operation(summary = "Cancel a booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    @Operation(summary = "Check-in to a booking")
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<String> checkInBooking(@PathVariable Long id) {
        bookingService.checkInBooking(id);
        return ResponseEntity.ok("Checked in successfully.");
    }

    @Operation(summary = "Check-out from a booking")
    @PatchMapping("/{id}/check-out")
    public ResponseEntity<String> checkOutBooking(@PathVariable Long id) {
        bookingService.checkOutBooking(id);
        return ResponseEntity.ok("Checked out successfully.");
    }

    // Extracts the user ID from the JWT token in the Authorization header
     private  Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        jwtUtil.validateToken(token);

        String email = jwtUtil.getEmailFromToken(token);
        return jwtUtil.getUserIdFromEmail(email);
    }
}
