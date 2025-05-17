package com.ohbs.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDTO {
    
    @Schema(description = "Unique identifier of the admin", example = "1")
    private Long id;

    @Schema(description = "First name of the admin", example = "John")
    private String firstName;

    @Schema(description = "Last name of the admin", example = "Doe")
    private String lastName;

    @Schema(description = "Phone number of the admin", example = "+1234567890")
    private String phone;

    @Schema(description = "Email of the admin", example = "admin@example.com")
    private String email;

    @Schema(description = "Username of the admin", example = "john_admin")
    private String username;
}
