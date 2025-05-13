package com.ohbs.bookings.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.ohbs.common.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;


@Data
@Entity
public class Bookings {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
	private User customer;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
	private Room room; // import the room model here
	
	
	@Column(name = "check_in_date", nullable = false)
	private LocalDate checkInDate;
	
	@Column(name = "check_out_date", nullable = false)
	private LocalDate checkOutDate;
	
	@Column(name = "total_price", nullable = false)
	private Double totalprice;
	
	@Column(name = "booking_status", nullable = false)
	private BookingStatus status;
	
	
	@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

	@Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
	
}
