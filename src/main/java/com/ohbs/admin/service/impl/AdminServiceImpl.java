package com.ohbs.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.exception.CustomerNotFoundException;
import com.ohbs.Customer.model.Customer;
import com.ohbs.Customer.repository.CustomerRepository;
import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;
import com.ohbs.admin.exception.AdminNotFoundException;
import com.ohbs.admin.model.Admin;
import com.ohbs.admin.repository.AdminRepository;
import com.ohbs.admin.service.AdminService;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.auth.service.AuthService;
import com.ohbs.common.model.Role;
import com.ohbs.common.model.User;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final AuthService userService;

    // ---------- ADMIN ----------

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO dto, RegisterUserDTO regdto) {
        // Register user with Role.ADMIN
        User user = userService.registerUser(regdto, Role.ADMIN);

        // Build and save Admin entity
        Admin admin = Admin.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .user(user)
                .build();

        adminRepository.save(admin);
        return mapToAdminResponseDTO(admin);
    }

    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found"));
        return mapToAdminResponseDTO(admin);
    }

    @Override
    public AdminResponseDTO getAdminByUserId(Long userId) {
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new AdminNotFoundException("Admin with User ID " + userId + " not found"));
        return mapToAdminResponseDTO(admin);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(this::mapToAdminResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found"));

        // Update fields
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setPhone(dto.getPhone());

        adminRepository.save(admin);
        return mapToAdminResponseDTO(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found"));
        adminRepository.delete(admin);
    }


    // ---------- CUSTOMER ----------

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto, RegisterUserDTO regdto) {
        User user = userService.registerUser(regdto, Role.CUSTOMER);

        Customer customer = Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .user(user)
                .build();

        return mapToCustomerResponseDTO(customerRepository.save(customer));
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToCustomerResponseDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setDeleted(true);
        customerRepository.save(customer);
    }


    // ---------- MANAGER ----------

    @Override
    public ManagerResponseDTO createManager(ManagerRequestDTO dto, RegisterUserDTO regdto) {
        User user = userService.registerUser(regdto, Role.MANAGER);

        Manager manager = Manager.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
//                .hotelName(dto.getHotelName())
                .user(user)
                .build();

        return mapToManagerResponseDTO(managerRepository.save(manager));
    }

    @Override
    public List<ManagerResponseDTO> getAllManagers() {
        return managerRepository.findAll().stream()
                .map(this::mapToManagerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ManagerResponseDTO getManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        return mapToManagerResponseDTO(manager);
    }

    @Override
    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }

    // ---------- Mappers ----------

    private AdminResponseDTO mapToAdminResponseDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phone(admin.getPhone())
                .email(admin.getUser().getEmail())
                .username(admin.getUser().getUsername())
                .build();
    }


    private CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .email(customer.getUser().getEmail())
                .build();
    }

    private ManagerResponseDTO mapToManagerResponseDTO(Manager manager) {
        return ManagerResponseDTO.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phone(manager.getPhone())
//                .hotelName(manager.getHotelName())
                .email(manager.getUser().getEmail())
                .build();
    }
}
