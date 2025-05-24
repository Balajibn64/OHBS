package com.ohbs.admin.service;

import java.util.List;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    // ADMIN MANAGEMENT
    AdminResponseDTO createAdmin(AdminRequestDTO dto, RegisterUserDTO regdto);
    AdminResponseDTO getAdminById(Long id);
    AdminResponseDTO getAdminByUserId(Long userId);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto , RegisterUserDTO userDto);
    void deleteAdmin(Long id);
    void uploadProfileImage(Long userId, MultipartFile imageFile);

    // CUSTOMER MANAGEMENT
    CustomerResponseDTO createCustomer(CustomerRequestDTO dto, RegisterUserDTO regdto);
    List<CustomerResponseDTO> getAllCustomers();
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto, RegisterUserDTO userDto);
    void deleteCustomer(Long id);

    // MANAGER MANAGEMENT
    ManagerResponseDTO createManager(ManagerRequestDTO dto, RegisterUserDTO regdto);
    List<ManagerResponseDTO> getAllManagers();
    ManagerResponseDTO getManagerById(Long id);
    ManagerResponseDTO updateManager(Long id, ManagerRequestDTO dto, RegisterUserDTO userDto);
    void deleteManager(Long id);
}
