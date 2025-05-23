package com.ohbs.payments.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohbs.payments.dto.PaymentRequestDTO;
import com.ohbs.payments.dto.PaymentResponseDTO;
import com.ohbs.payments.model.Payment;
import com.ohbs.payments.repository.PaymentRepository;
import com.ohbs.payments.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
    private final PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

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

    @PostMapping("/create-order")
    public String createOrder(@RequestParam Double amount, @RequestParam Long bookingId) throws Exception {
        RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // Amount in paise
        options.put("currency", "INR");
        options.put("receipt", "booking_" + bookingId);

        Order order = client.orders.create(options);
        return order.toString();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) throws Exception {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");
        Long bookingId = Long.valueOf(payload.get("bookingId"));
        Double amount = Double.valueOf(payload.get("amount"));

        String data = orderId + "|" + paymentId;
        String generatedSignature = hmacSHA256(data, razorpayKeySecret);

        if (generatedSignature.equals(signature)) {
            // Save payment details
            Payment payment = Payment.builder()
                .bookingId(bookingId)
                .paymentDate(java.time.LocalDateTime.now())
                .amount(amount)
                .paymentMethod("RAZORPAY")
                .paymentStatus("SUCCESS")
                .transactionId(paymentId)
                .build();
            paymentRepository.save(payment);
            //send the booking and payments details to the customer through mail.
            return ResponseEntity.ok("Payment verified and saved.");
        } else {
            return ResponseEntity.badRequest().body("Invalid signature");
        
        }
    }

    private String hmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return new String(Base64.getEncoder().encode(sha256_HMAC.doFinal(data.getBytes())));
    }
}


