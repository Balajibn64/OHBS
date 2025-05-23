package com.ohbs.manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ManagerRequestDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phone;

    private Long hotelId;
}