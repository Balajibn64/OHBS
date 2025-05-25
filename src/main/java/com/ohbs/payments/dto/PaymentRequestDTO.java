package com.ohbs.payments.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method length must be less than 50")
    private String paymentMethod;

    @NotBlank(message = "Payment status is required")
    @Size(max = 20, message = "Payment status length must be less than 20")
    private String paymentStatus;

    @NotBlank(message = "Transaction ID is required")
    @Size(max = 100, message = "Transaction ID length must be less than 100")
    private String transactionId;
}
