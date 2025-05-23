package com.ohbs.hotelmgt.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.model.HotelImage;
import com.ohbs.room.dto.RoomResponseDTO;
import com.ohbs.room.dto.RoomTypePriceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String description;
    private double rating;
    private List<String> imageUrls;
    private List<RoomTypePriceDTO> roomTypesAndPrices;
    private List<RoomResponseDTO> rooms;

    public static HotelResponseDTO fromEntity(Hotel hotel) {
        
        List<String> imageUrls = hotel.getImages() != null
                ? hotel.getImages().stream().map(HotelImage::getImageUrl).toList()
                : List.of();

        List<RoomResponseDTO> rooms = hotel.getRooms() != null
                ? hotel.getRooms().stream().map(RoomResponseDTO::fromEntity).collect(Collectors.toList())
                : List.of();

        List<RoomTypePriceDTO> roomTypesAndPrices = hotel.getRooms() != null
                ? hotel.getRooms().stream()
                    .map(room -> new RoomTypePriceDTO(room.getRoomType(), room.getPricePerDay()))
                    .collect(Collectors.toList())
                : List.of();

        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                hotel.getRating(),
                imageUrls,
                roomTypesAndPrices,
                rooms
        );
    }
}
