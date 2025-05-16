package com.ohbs.Customer.service;

import com.ohbs.Customer.dto.*;
import com.ohbs.Customer.dto.*;

public interface CustomerService {

    CustomerResponseDTO createCustomerProfile(CustomerRequestDTO dto, Long userId);

    CustomerResponseDTO getCustomerProfile(Long userId);

    CustomerResponseDTO updateCustomerProfile(Long userId, CustomerRequestDTO dto);

    void deleteCustomerProfile(Long userId);
}
