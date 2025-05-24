package com.ohbs.admin.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import com.ohbs.auth.exception.UserAlreadyExistsException;
import com.ohbs.auth.service.AuthService;
import com.ohbs.common.model.Role;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.exception.ManagerNotFoundException;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.repository.ManagerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final UserRepository userResposity;
    private final AuthService userService;
    private final Cloudinary cloudinary;

    // ---------- ADMIN ----------

    @Override
    @Transactional
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
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto , RegisterUserDTO userDto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found"));

        // Update all fields in Admin
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setPhone(dto.getPhone());
        // Update user details if present in DTO
        if (admin.getUser() != null) {
            if (userDto.getEmail() != null) admin.getUser().setEmail(userDto.getEmail());
            if (userDto.getUsername() != null) admin.getUser().setUsername(userDto.getUsername());
            if (userDto.getPassword() != null) admin.getUser().setPassword(userDto.getPassword());
        }
        adminRepository.save(admin);
        return mapToAdminResponseDTO(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found"));
        adminRepository.delete(admin);
    }

    // Save or update admin profile image
    public void uploadProfileImage(Long userId, MultipartFile imageFile) {
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new AdminNotFoundException("Admin profile not found"));
        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            admin.setProfileImageUrl(imageUrl);
            adminRepository.save(admin);
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    // ---------- CUSTOMER ----------

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto, RegisterUserDTO regdto) {
        // Check if a user with the same email or username already exists
        if (userResposity.existsByEmail(regdto.getEmail())) {
            throw new UserAlreadyExistsException("A customer with this email already exists.");
        }
        if (userResposity.existsByUsername(regdto.getUsername())) {
            throw new UserAlreadyExistsException("A customer with this username already exists.");
        }
        User user = userService.registerUser(regdto, Role.CUSTOMER);

        Customer customer = Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .dob(dto.getDob())
                .gender(dto.getGender())
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
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return mapToCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request, RegisterUserDTO userDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setDob(request.getDob());
        customer.setGender(request.getGender());
        if (customer.getUser() != null && userDto != null) {
            if (userDto.getEmail() != null) customer.getUser().setEmail(userDto.getEmail());
            if (userDto.getUsername() != null) customer.getUser().setUsername(userDto.getUsername());
            if (userDto.getPassword() != null) customer.getUser().setPassword(userDto.getPassword());
        }
        customerRepository.save(customer);
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
        // Check if a user with the same email or username already exists
        if (userResposity.existsByEmail(regdto.getEmail())) {
            throw new UserAlreadyExistsException("A manager with this email already exists.");
        }
        if (userResposity.existsByUsername(regdto.getUsername())) {
            throw new UserAlreadyExistsException("A manager with this username already exists.");
        }
        User user = userService.registerUser(regdto, Role.MANAGER);

        Manager manager = Manager.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
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
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));
        return mapToManagerResponseDTO(manager);
    }

    @Override
    public ManagerResponseDTO updateManager(Long id, ManagerRequestDTO request, RegisterUserDTO userDto) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));
        manager.setFirstName(request.getFirstName());
        manager.setLastName(request.getLastName());
        manager.setPhone(request.getPhone());
//        if (request.getProfileImageUrl() != null) {
//            manager.setProfileImageUrl(request.getProfileImageUrl());
//        }
        if (manager.getUser() != null && userDto != null) {
            if (userDto.getEmail() != null) manager.getUser().setEmail(userDto.getEmail());
            if (userDto.getUsername() != null) manager.getUser().setUsername(userDto.getUsername());
            if (userDto.getPassword() != null) manager.getUser().setPassword(userDto.getPassword());
        }
        managerRepository.save(manager);
        return mapToManagerResponseDTO(manager);
    }

    @Override
    public void deleteManager(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));
        manager.setDeleted(true); // Soft delete
        managerRepository.save(manager);
    }

    // ---------- Mappers ----------

    private AdminResponseDTO mapToAdminResponseDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .id(admin.getId())
                .userId(admin.getUser() != null ? admin.getUser().getId() : null)
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phone(admin.getPhone())
                .profileImageUrl(admin.getProfileImageUrl())
                .email(admin.getUser() != null ? admin.getUser().getEmail() : null)
                .username(admin.getUser() != null ? admin.getUser().getUsername() : null)
                .build();
    }

    private CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .userId(customer.getUser() != null ? customer.getUser().getId() : null)
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .username(customer.getUser() != null ? customer.getUser().getUsername() : null)
                .email(customer.getUser() != null ? customer.getUser().getEmail() : null)
                .profileImageUrl(customer.getProfileImageUrl())
                .dob(customer.getDob())
                .gender(customer.getGender())
                .build();
    }

    private ManagerResponseDTO mapToManagerResponseDTO(Manager manager) {
        return ManagerResponseDTO.builder()
                .id(manager.getId())
                .userId(manager.getUser() != null ? manager.getUser().getId() : null)
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phone(manager.getPhone())
                .hotelId(manager.getHotels() != null && !manager.getHotels().isEmpty() ? manager.getHotels().get(0).getId() : null)
                .email(manager.getUser() != null ? manager.getUser().getEmail() : null)
                .profileImageUrl(manager.getProfileImageUrl())
                .username(manager.getUser() != null ? manager.getUser().getUsername() : null)
                .build();
    }
}
