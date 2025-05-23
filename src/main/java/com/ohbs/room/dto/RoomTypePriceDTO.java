package com.ohbs.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypePriceDTO {
    private String roomType;
    private Double pricePerDay;
}