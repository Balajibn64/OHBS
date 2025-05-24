package com.ohbs.bookings.repository;

import java.util.List;
import java.util.Optional;
import com.ohbs.bookings.model.Booking;
import com.ohbs.bookings.model.BookingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus bookingStatus); // Corrected: BookingStatus enum type

    Optional<Booking> findByCustomerIdAndRoomId(Long customerId, Long roomId);
    
    List<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status); // Additional for status by customer

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.payment WHERE b.id = :id")
    Optional<Booking> findByIdWithPayment(@Param("id") Long id);

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.payment WHERE b.customer.id = :customerId")
    List<Booking> findByCustomerIdWithPayment(@Param("customerId") Long customerId);
}
