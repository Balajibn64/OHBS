package com.ohbs.payments.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long id;
    private Long bookingId;
    private LocalDateTime paymentDate;
    private Double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
}
