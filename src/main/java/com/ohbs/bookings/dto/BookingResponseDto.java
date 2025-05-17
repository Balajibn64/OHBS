package com.ohbs.bookings.dto;

import java.time.LocalDate;

import com.ohbs.bookings.model.BookingStatus;

import lombok.Data;

@Data
public class BookingResponseDto {

	private Long id;
    private Long customerId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalPrice;
    private BookingStatus bookingStatus;
    private String customerName;
    private String roomNumber;
}
