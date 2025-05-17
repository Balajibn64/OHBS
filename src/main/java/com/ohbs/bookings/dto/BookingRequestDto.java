package com.ohbs.bookings.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class BookingRequestDto {

    @NotNull
    private Long customerId;

    @NotNull
    private Long roomId;

    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull
    @FutureOrPresent
    private LocalDate checkOutDate;

    @NotNull
    @Positive
    private Double totalPrice;
}
