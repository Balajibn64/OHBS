package com.ohbs.hotelmgt.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class HotelRequestDTO {

    @NotBlank(message = "Hotel name cannot be empty")
    private String name;

    @NotBlank(message = "Location must be provided")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotBlank
    @DecimalMin(value = "1.0", inclusive = true, message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    private double rating;

    @NotBlank(message = "Image URL cannot be empty")
    private String imageUrl;
}
