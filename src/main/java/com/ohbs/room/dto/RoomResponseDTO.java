package com.ohbs.room.dto;

import java.util.List;

import com.ohbs.room.model.Room;

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
    private List<String> imageUrls;

    public static RoomResponseDTO fromEntity(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .pricePerDay(room.getPricePerDay())
                .description(room.getDescription())
                .isAvailable(room.isAvailable())
                .hotelId(room.getHotel() != null ? room.getHotel().getId() : null)
                .hotelName(room.getHotel() != null ? room.getHotel().getName() : null)
                .imageUrls(room.getRoomImages() != null
                    ? room.getRoomImages().stream().map(img -> img.getImageUrl()).toList()
                    : List.of())
                .build();
    }
}
