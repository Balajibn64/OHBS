package com.ohbs.bookings.service;

import java.util.List;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.model.Booking;

public interface BookingService {

    Booking createBooking(BookingRequestDto dto);

    Booking getBooking(Long id);

    List<Booking> getBookingsByCustomer(Long customerId);

    List<Booking> getBookingsByRoom(Long roomId);

    Booking updateBooking(Long id, BookingRequestDto dto);

    void cancelBooking(Long id);

    void checkInBooking(Long id);

    void checkOutBooking(Long id);
}
