package com.ohbs.admin.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
