package com.ohbs.bookings.service;

import java.util.List;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.dto.BookingResponseDto;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto dto);

    BookingResponseDto getBooking(Long id);

    List<BookingResponseDto> getBookingsByCustomer(Long customerId);

    List<BookingResponseDto> getBookingsByRoom(Long roomId);

    BookingResponseDto updateBooking(Long id, BookingRequestDto dto);

    void cancelBooking(Long id);

    void checkInBooking(Long id);

    void checkOutBooking(Long id);
}
