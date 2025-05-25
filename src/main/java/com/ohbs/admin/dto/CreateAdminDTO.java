package com.ohbs.admin.dto;

import com.ohbs.auth.dto.RegisterUserDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminDTO {
    private AdminRequestDTO adminRequestDTO;
    private RegisterUserDTO registerUserDTO;
}
