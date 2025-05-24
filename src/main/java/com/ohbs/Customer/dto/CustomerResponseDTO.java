package com.ohbs.Customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {

    @Schema(description = "Customer ID")
    private Long id;

    @Schema(description = "User ID associated with the customer")
    private Long userId;

    @Schema(description = "First name of the customer")
    private String firstName;

    @Schema(description = "Last name of the customer")
    private String lastName;

    @Schema(description = "Phone number of the customer")
    private String phone;

    @Schema(description = "Address of the customer")
    private String address;

    @Schema(description = "Email of the customer")
    private String email;

    @Schema(description = "Profile image URL of the customer")
    private String profileImageUrl;

    @Schema(description = "Date of birth of the customer")
    private LocalDate dob;

    @Schema(description = "Gender of the customer (Male, Female, or Other)")
    private String gender;

    @Schema(description = "Username of the customer")
    private String username;
}
