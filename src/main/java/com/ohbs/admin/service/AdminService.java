package com.ohbs.admin.service;

import java.util.List;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;

public interface AdminService {

    // ADMIN MANAGEMENT
    AdminResponseDTO createAdmin(AdminRequestDTO dto, RegisterUserDTO regdto);
    AdminResponseDTO getAdminById(Long id);
    AdminResponseDTO getAdminByUserId(Long userId);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto);
    void deleteAdmin(Long id);

    // CUSTOMER MANAGEMENT
    CustomerResponseDTO createCustomer(CustomerRequestDTO dto, RegisterUserDTO regdto);
    List<CustomerResponseDTO> getAllCustomers();
    CustomerResponseDTO getCustomerById(Long id);
    void deleteCustomer(Long id);

    // MANAGER MANAGEMENT
    ManagerResponseDTO createManager(ManagerRequestDTO dto, RegisterUserDTO regdto);
    List<ManagerResponseDTO> getAllManagers();
    ManagerResponseDTO getManagerById(Long id);
    void deleteManager(Long id);
}
