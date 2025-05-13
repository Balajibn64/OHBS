package com.ohbs.hotelmgt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hotel name cannot be empty")
    private String name; 

    @NotBlank(message = "Location must be provided")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    @Column(length = 1000)
    private String description;

    @DecimalMin(value = "1.0", inclusive = true, message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    private double rating;

    @Column(name = "image_url")
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(https?://).+\\.(jpg|jpeg|png|webp)$", message = "Image URL must start with http:// or https:// and end with a valid image extension (jpg, png, webp)")
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelImage> images = new ArrayList<>();

    /**
     * Used for soft delete.
     * When true, this hotel is considered deleted and will not show in list APIs.
     */
    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isDeleted = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
