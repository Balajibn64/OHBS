package com.ohbs.bookings.repository;

import java.util.List;
import java.util.Optional;
import com.ohbs.bookings.model.Bookings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Bookings, Long>{

	List<Bookings> findByCustomerId(Long customerId);
	
	List<Bookings> findByRoomId(Long roomId);
	
	List<Bookings> findByBookingStatus(String bookingStatus);
	
	Optional<Bookings> findByCustomerIdAndRoomId(Long customerId, Long roomId);
}
