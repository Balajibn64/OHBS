package com.ohbs.manager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private Long hotelId;
    private String email;
    private String profileImageUrl;
}