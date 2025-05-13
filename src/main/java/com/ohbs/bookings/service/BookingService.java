package com.ohbs.bookings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.dto.BookingResponseDto;
import com.ohbs.bookings.model.BookingStatus;
import com.ohbs.bookings.model.Bookings;
import com.ohbs.bookings.repository.BookingRepository;
import com.ohbs.common.exception.BadRequestException;
import com.ohbs.common.exception.ResourceNotFoundException;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	Private RoomRpository roomRepo;//room repo needs to be created
	

	public Bookings createBooking(BookingRequestDto dto) {
		User customer = userRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Room room = roomRepo.findById(dto.getRoomId())// need to import room model
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Bookings booking = new Bookings();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setTotalprice(dto.getTotalPrice());
        booking.setStatus(dto.getBookingStatus());

        Bookings saved = bookingRepo.save(booking);
        return saved;// need to check whether to send as dto or saved object
	}
	
	public Bookings getBooking(Long id) {
		return bookingRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));// returned the Bookings instead of Response Dto
	}
	
	public List<Bookings> getBookingsByCustomer(Long customerId){
		return bookingRepo.findByCustomerId(customerId);// it will return the list of bookings not dto
	}
	
	public List<Bookings> getBookingsByRoom(Long roomId){
		return bookingRepo.findByRoomId(roomId);// it will return the list of bookings not dto
	}
	
	public Bookings updateBooking(Long id, BookingRequestDto dto) {
		Bookings booking=bookingRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		User customer = userRepo.findById(dto.getCustomerId())
		        .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		Room room = roomRepo.findById(dto.getRoomId())// need to import room model
		        .orElseThrow(() -> new ResponseNotFoundException("Room not found"));
		
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setTotalprice(dto.getTotalPrice());
        booking.setStatus(dto.getBookingStatus());

        Bookings saved = bookingRepo.save(booking);
        return saved;
        //return new BookingResponseDto(saved);// need to check whether to send as dto or saved object
        
        //also need to check whether the booking is cancelled or not or any other details timings which impact the rates etc., should be considered.
	}
	
	public void cancelBooking(Long id) {
		Bookings booking=bookingRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Booking not available"));
		if(booking.getStatus().name().equals("BOOKED")) {
			booking.setStatus(BookingStatus.CANCELLED);
			// Also need to trigger the reverse transaction of payment is to be done here
		}else if(booking.getStatus().name().equals("CHECKED_IN")){
			throw new BadRequestException("Already User Checked-In. Hence unable to cancel");
		}
	}
	
	
}
