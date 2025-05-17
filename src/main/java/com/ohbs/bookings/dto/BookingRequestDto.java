package com.ohbs.bookings.dto;

import java.time.LocalDate;

import com.ohbs.bookings.model.BookingStatus;

import lombok.Data;

@Data
public class BookingRequestDto {

	private Long customerId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalPrice;
    private BookingStatus bookingStatus;
}
