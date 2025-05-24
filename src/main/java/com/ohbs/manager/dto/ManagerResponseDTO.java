package com.ohbs.manager.dto;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ManagerResponseDTO {
    @Schema(description = "Manager ID")
    private Long id;

    @Schema(description = "First name of the manager")
    private String firstName;

    @Schema(description = "Last name of the manager")
    private String lastName;

    @Schema(description = "Phone number of the manager")
    private String phone;

    @Schema(description = "Hotel ID managed by the manager")
    private Long hotelId;

    @Schema(description = "Email of the manager")
    private String email;

    @Schema(description = "Profile image URL of the manager")
    private String profileImageUrl;

    @Schema(description = "User ID associated with the manager")
    private Long userId;

    @Schema(description = "Username of the manager")
    private String username;
}