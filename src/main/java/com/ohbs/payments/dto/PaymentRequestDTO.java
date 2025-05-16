package com.ohbs.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long bookingId;
    private Double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
}
