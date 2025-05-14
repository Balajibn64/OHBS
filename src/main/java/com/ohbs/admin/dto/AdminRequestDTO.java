package com.ohbs.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequestDTO {
    private String firstName;
    private String lastName;
    private String phone;
}

