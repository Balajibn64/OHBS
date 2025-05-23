package com.ohbs.Customer.service;

import org.springframework.web.multipart.MultipartFile;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;

public interface CustomerService {

//    CustomerResponseDTO createCustomerProfile(CustomerRequestDTO dto, Long userId);

    CustomerResponseDTO getCustomerProfile(Long userId);

    CustomerResponseDTO updateCustomerProfile(Long userId, CustomerRequestDTO dto);
    
    void uploadProfileImage(Long userId, MultipartFile imageFile);

    void deleteCustomerProfile(Long userId);
}
