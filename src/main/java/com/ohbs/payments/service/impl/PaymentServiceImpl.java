package com.ohbs.payments.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ohbs.payments.dto.PaymentRequestDTO;
import com.ohbs.payments.dto.PaymentResponseDTO;
import com.ohbs.payments.exception.DuplicateEntryException;
import com.ohbs.payments.exception.PaymentCancellationException;
import com.ohbs.payments.exception.PaymentNotFoundException;
import com.ohbs.payments.exception.PaymentProcessingException;
import com.ohbs.payments.model.Payment;
import com.ohbs.payments.repository.PaymentRepository;
import com.ohbs.payments.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        if (dto.getTransactionId() == null || dto.getTransactionId().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID must not be null or empty");
        }

        if (paymentRepository.existsByTransactionId(dto.getTransactionId())) {
            throw new DuplicateEntryException("Payment with this transaction ID already exists");
        }

        Payment payment = Payment.builder()
                .bookingId(dto.getBookingId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus().toUpperCase() : "PENDING")
                .transactionId(dto.getTransactionId())
                .paymentDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException ex) {
            throw new PaymentProcessingException("Failed to create payment due to data integrity violation.");
        }

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
    @Transactional
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));

        if ("COMPLETED".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new PaymentProcessingException("Cannot update a completed payment");
        }

        if (dto.getTransactionId() != null && !dto.getTransactionId().equals(payment.getTransactionId())) {
            if (paymentRepository.existsByTransactionId(dto.getTransactionId())) {
                throw new DuplicateEntryException("Payment with this transaction ID already exists");
            }
            payment.setTransactionId(dto.getTransactionId());
        }

        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus().toUpperCase() : payment.getPaymentStatus());
        payment.setUpdatedAt(LocalDateTime.now());

        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException ex) {
            throw new PaymentProcessingException("Failed to update payment due to data integrity violation.");
        }

        return mapToResponse(payment);
    }

    @Override
    @Transactional
    public void processPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));

        if ("COMPLETED".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new PaymentProcessingException("Payment is already completed");
        }

        payment.setPaymentStatus("COMPLETED");
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));

        if ("COMPLETED".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new PaymentCancellationException("Cannot cancel a completed payment");
        }

        payment.setPaymentStatus("CANCELLED");
        payment.setUpdatedAt(LocalDateTime.now());
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
