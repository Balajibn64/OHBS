package com.ohbs.admin.dto;

import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateManagerDTO {
    private ManagerRequestDTO managerRequestDTO;
    private RegisterUserDTO registerUserDTO;
}
