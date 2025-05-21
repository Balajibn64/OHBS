package com.ohbs.hotelmgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "image_url", nullable = false)
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(https?://).+\\.(jpg|jpeg|png|webp)$", message = "Image URL must start with http:// or https:// and end with a valid image extension (jpg, png, webp)")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    private Hotel hotel;
}
