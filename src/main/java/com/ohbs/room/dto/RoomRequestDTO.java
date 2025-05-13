package com.ohbs.room.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDTO {

    @NotNull
    private Long hotelId;

    @NotBlank
    private String roomNumber;

    @NotBlank
    private String roomType;

    @NotNull
    @DecimalMin("0.0")
    private Double pricePerDay;

    private String description;

    private Boolean isAvailable = true;
}
