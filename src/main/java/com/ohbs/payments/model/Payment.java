package com.ohbs.payments.model;

import java.time.LocalDateTime;

import com.ohbs.bookings.model.Booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

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
