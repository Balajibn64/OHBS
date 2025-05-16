package com.ohbs.payments.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for Payment details")
public class PaymentResponseDTO {

    @Schema(description = "Payment ID", example = "1")
    private Long id;

    @Schema(description = "Booking ID linked to the payment", example = "123")
    private Long bookingId;

    @Schema(description = "Date and time when the payment was made", example = "2025-05-16T19:14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentDate;

    @Schema(description = "Amount paid", example = "2500.00")
    private Double amount;

    @Schema(description = "Method of payment", example = "Credit Card")
    private String paymentMethod;

    @Schema(description = "Current payment status", example = "COMPLETED")
    private String paymentStatus;

    @Schema(description = "Unique transaction identifier", example = "TXN123456789")
    private String transactionId;
}
