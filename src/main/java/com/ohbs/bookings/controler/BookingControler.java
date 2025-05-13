package com.ohbs.bookings.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.model.Bookings;
import com.ohbs.bookings.service.BookingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/bookings")
public class BookingControler {

	@Autowired
	public BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<Bookings> createBooking(@RequestBody BookingRequestDto dto) {
		return ResponseEntity.ok(bookingService.createBooking(dto));//not returned the dto instead returned the created object
	}
	
	@GetMapping("/{Id}")
	public ResponseEntity<?> getBookingsById(@PathVariable Long Id){
		return ResponseEntity.ok(bookingService.getBooking(Id));//not returned the dto instead returned the created object
	
	}
	
	@GetMapping("/customer/{Id}")
	public ResponseEntity<?> getCustomerBokkings(@PathVariable Long Id){
//		to get all the customer bookings 
		return ResponseEntity.ok(bookingService.getBookingsByCustomer(Id));//not returned the dto instead returned the created object
	}
	
	@GetMapping("/room/{Id}")
	public ResponseEntity<?> getRoomBokkings(@PathVariable Long Id){
//		to get all the room bookings 
		return ResponseEntity.ok(bookingService.getBookingsByRoom(Id));//not returned the dto instead returned the created object
	}
	
	@PutMapping("/{Id}")
	public ResponseEntity<Bookings> updateBooking(@PathVariable Long Id, @RequestBody BookingRequestDto dto) {
		return ResponseEntity.ok(bookingService.updateBooking(Id,dto));//not returned the dto instead returned the created object
	}
	
	public ResponseEntity<String> deleteBooking(@PathVariable Long Id){
		bookingService.cancelBooking(Id);
		return ResponseEntity.ok("Booking cancelled");
	}
}
