package com.ohbs.bookings.dto;

import java.time.LocalDate;

import com.ohbs.bookings.model.BookingStatus;
import com.ohbs.payments.dto.PaymentResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "Detailed information of a booking response")
@Builder
public class BookingResponseDto {

    @Schema(description = "Unique identifier of the booking", example = "1")
    private Long id;

    @Schema(description = "Unique identifier of the customer associated with the booking", example = "101")
    private Long customerId;

    @Schema(description = "Full name of the customer who made the booking", example = "John Doe")
    private String customerName;

    @Schema(description = "Unique identifier of the booked room", example = "202")
    private Long roomId;

    @Schema(description = "Room number of the booked room", example = "A-101")
    private String roomNumber;

    @Schema(description = "Type of the booked room", example = "Deluxe Suite")
    private String roomType;

    @Schema(description = "Hotel name where the room is booked", example = "Grand Hotel")
    private String hotelName;

    @Schema(description = "Check-in date for the booking", example = "2025-06-01", type = "string", format = "date")
    private LocalDate checkInDate;

    @Schema(description = "Check-out date for the booking", example = "2025-06-05", type = "string", format = "date")
    private LocalDate checkOutDate;

    @Schema(description = "Total price of the booking in local currency", example = "5000.00", type = "number", format = "double")
    private Double totalPrice;

    @Schema(description = "Current status of the booking", 
            example = "CONFIRMED", 
            type = "string", 
            allowableValues = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"})
    private BookingStatus bookingStatus;

    @Schema(description = "Payment details for the booking")
    private PaymentResponseDTO payment;

    @Schema(description = "The date and time when the booking was created", example = "2025-05-15T10:15:30")
    private String createdAt;

    @Schema(description = "The date and time when the booking was last updated", example = "2025-05-16T12:45:10")
    private String updatedAt;
}
