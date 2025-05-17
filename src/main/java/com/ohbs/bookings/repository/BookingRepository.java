package com.ohbs.bookings.repository;

import java.util.List;
import java.util.Optional;
import com.ohbs.bookings.model.Booking;
import com.ohbs.bookings.model.BookingStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus bookingStatus); // Corrected: BookingStatus enum type

    Optional<Booking> findByCustomerIdAndRoomId(Long customerId, Long roomId);
    
    List<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status); // Additional for status by customer
}
