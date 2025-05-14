package com.ohbs.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDTO {

    private Long id;
    private String roomNumber;
    private String roomType;
    private Double pricePerDay;
    private String description;
    private Boolean isAvailable;
    private Long hotelId;
    private String hotelName;
}
