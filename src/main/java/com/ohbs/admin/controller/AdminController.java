package com.ohbs.admin.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.exception.CustomerNotFoundException;
import com.ohbs.Customer.model.Customer;
import com.ohbs.Customer.repository.CustomerRepository;
import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;
import com.ohbs.admin.dto.CreateAdminDTO;
import com.ohbs.admin.dto.CreateCustomerDTO;
import com.ohbs.admin.dto.CreateManagerDTO;
import com.ohbs.admin.service.AdminService;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.common.exception.UnauthorizedException;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.service.ManagerService;
import com.ohbs.security.jwt.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "APIs for managing admins, customers, and managers")
public class AdminController {

    private final AdminService adminService;
    private final ManagerService managerService;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    // --------- ADMIN MANAGEMENT --------- //

    @Operation(summary = "Create a new admin", description = "Creates an admin for a specific user")
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDTO> createAdmin(@Valid @RequestBody CreateAdminDTO requestDTO) {
        return ResponseEntity.ok(
            adminService.createAdmin(
                requestDTO.getAdminRequestDTO(),
                requestDTO.getRegisterUserDTO()
            )
        );
    }

    @Operation(summary = "Get an admin by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @Operation(summary = "Get all admins")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @Operation(summary = "Update an admin")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable Long id,
                                                        @Valid @RequestBody AdminRequestDTO adminRequestDTO,
                                                        @Valid @RequestBody RegisterUserDTO userDto) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminRequestDTO, userDto));
    }

    @Operation(summary = "Delete an admin")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Admin profile successfully deleted.");
    }

    @Operation(summary = "Upload or update admin profile image")
    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("image") MultipartFile imageFile,
            HttpServletRequest request) {
        Long userId = extractUserId(request);
        adminService.uploadProfileImage(userId, imageFile);
        return ResponseEntity.ok("Profile image uploaded successfully");
    }
    	
    
    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        jwtUtil.validateToken(token);

        String email = jwtUtil.getEmailFromToken(token);
        return jwtUtil.getUserIdFromEmail(email);
    }
 
    // --------- CUSTOMER MANAGEMENT --------- //

    @Operation(summary = "List all customers")
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> listAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }

    @Operation(summary = "Get a customer by ID")
    @GetMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCustomerById(id));
    }

    @Operation(summary = "Create a customer profile")
    @PostMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CreateCustomerDTO requestDTO) {
        return ResponseEntity.ok(
            adminService.createCustomer(
                requestDTO.getCustomerRequestDTO(),
                requestDTO.getRegisterUserDTO()
            )
        );
    }

    @Operation(summary = "Update a customer profile")
    @PutMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO dto, @Valid @RequestBody RegisterUserDTO userDto) {
        return ResponseEntity.ok(adminService.updateCustomer(id, dto, userDto));
    }

    @Operation(summary = "Delete a customer")
    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setDeleted(true);
        customerRepository.save(customer);

        return ResponseEntity.ok("Customer Deleted Successfully");
    }


    // --------- MANAGER MANAGEMENT --------- //

    @Operation(summary = "List all managers")
    @GetMapping("/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ManagerResponseDTO>> listAllManagers() {
        return ResponseEntity.ok(adminService.getAllManagers());
    }

    @Operation(summary = "Get a manager by ID")
    @GetMapping("/managers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDTO> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getManagerById(id));
    }

    @Operation(summary = "Create a manager profile")
    @PostMapping("/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDTO> createManager(
            @Valid @RequestBody CreateManagerDTO requestDTO) {
        return ResponseEntity.ok(
            adminService.createManager(
                requestDTO.getManagerRequestDTO(),
                requestDTO.getRegisterUserDTO()
            )
        );
    }

    @Operation(summary = "Update a manager profile")
    @PutMapping("/managers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerResponseDTO> updateManager(@PathVariable Long id, @Valid @RequestBody ManagerRequestDTO managerDto, @Valid @RequestBody RegisterUserDTO userDto) {
        return ResponseEntity.ok(adminService.updateManager(id, managerDto, userDto));
    }

    @Operation(summary = "Delete a manager")
    @DeleteMapping("/managers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteManager(@PathVariable Long id) {
        managerService.deleteManagerProfile(id);
        return ResponseEntity.ok("Manager Deleted Successfully");
    }
}
