package com.ohbs.bookings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.dto.BookingResponseDto;
import com.ohbs.bookings.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

    @Autowired
    private BookingService bookingService;

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

    @Operation(summary = "Get all bookings")
    @GetMapping
    public ResponseEntity<Object> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @Operation(summary = "Get all bookings for a customer")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCustomer(@PathVariable Long customerId) {
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
}
