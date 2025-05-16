package com.ohbs.payments.repository;

import com.ohbs.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);

    List<Payment> findByPaymentStatus(String paymentStatus);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByBookingIdAndPaymentStatus(Long bookingId, String paymentStatus);

    Page<Payment> findByBookingId(Long bookingId, Pageable pageable);

    Page<Payment> findByPaymentStatus(String paymentStatus, Pageable pageable);
   
    boolean existsByTransactionId(String transactionId);
}
