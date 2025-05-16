package com.ohbs.payments.service.impl;

import com.ohbs.payments.dto.*;
import com.ohbs.payments.entity.Payment;
import com.ohbs.payments.exception.PaymentNotFoundException;
import com.ohbs.payments.repository.PaymentRepository;
import com.ohbs.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Payment payment = Payment.builder()
                .bookingId(dto.getBookingId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus(dto.getPaymentStatus())
                .transactionId(dto.getTransactionId())
                .build();
        paymentRepository.save(payment);
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponseDTO getPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setTransactionId(dto.getTransactionId());
        paymentRepository.save(payment);
        return mapToResponse(payment);
    }

    @Override
    public void processPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
        payment.setPaymentStatus("Completed");
        paymentRepository.save(payment);
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }
}
