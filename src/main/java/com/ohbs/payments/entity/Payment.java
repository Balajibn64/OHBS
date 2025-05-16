package com.ohbs.payments.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
    name = "payments",
    indexes = {
        @Index(name = "idx_transaction_id", columnList = "transactionId"),
        @Index(name = "idx_booking_id", columnList = "bookingId")
    }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long bookingId;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double amount;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String paymentStatus;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
