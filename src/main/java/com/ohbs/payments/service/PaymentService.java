package com.ohbs.payments.service;

import com.ohbs.payments.dto.PaymentRequestDTO;
import com.ohbs.payments.dto.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO dto);
    PaymentResponseDTO getPayment(Long id);
    List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId);
    PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto);
    void processPayment(Long id);
}
