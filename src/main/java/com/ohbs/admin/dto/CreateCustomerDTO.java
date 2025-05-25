package com.ohbs.admin.dto;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDTO {
    private CustomerRequestDTO customerRequestDTO;
    private RegisterUserDTO registerUserDTO;
}
