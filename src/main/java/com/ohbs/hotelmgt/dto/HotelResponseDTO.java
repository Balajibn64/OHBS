package com.ohbs.hotelmgt.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.model.HotelImage;

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

    public static HotelResponseDTO fromEntity(Hotel hotel) {
        List<String> imageUrls = hotel.getImages() != null
                ? hotel.getImages().stream().map(HotelImage::getImageUrl).toList()
                : List.of();

        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                hotel.getRating(),
                imageUrls
        );
    }
}
