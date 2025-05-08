package com.ohbs.manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManagerRequestDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String phone;

    private Long hotelId;
}