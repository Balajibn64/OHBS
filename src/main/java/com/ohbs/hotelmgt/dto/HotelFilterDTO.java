package com.ohbs.hotelmgt.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HotelFilterDTO {
    private String location;
    private Double minRating;
    private Double maxRating;
    private Date checkinDate;
    private Date checkoutDate;
    private String sortBy;
    // Add more filter fields as needed
}