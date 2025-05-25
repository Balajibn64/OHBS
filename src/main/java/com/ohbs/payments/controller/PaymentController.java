package com.ohbs.payments.controller;

import com.ohbs.payments.dto.PaymentRequestDTO;
import com.ohbs.payments.dto.PaymentResponseDTO;
import com.ohbs.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBooking(bookingId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@PathVariable Long id, @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        paymentService.processPayment(id);
        return ResponseEntity.noContent().build();
    }
}
