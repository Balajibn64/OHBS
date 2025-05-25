// com.ohbs.auth.dto.LoginResponseDTO.java
package com.ohbs.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long managerId;
}
