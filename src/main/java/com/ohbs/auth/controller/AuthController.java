package com.ohbs.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.repository.AdminRepository;
import com.ohbs.admin.service.impl.AdminServiceImpl;
import com.ohbs.auth.dto.LoginRequestDTO;
import com.ohbs.auth.dto.RegisterCustomerDTO;
import com.ohbs.auth.dto.RegisterManagerDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.auth.service.AuthService;
import com.ohbs.common.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
    private final AdminRepository adminRepository;
	
	@Autowired
    private final AdminServiceImpl adminServiceImpl;
    
	@Autowired
    private final UserRepository userRepository;

    @Autowired
    private AuthService authService;

    AuthController(AdminServiceImpl adminServiceImpl, AdminRepository adminRepository , UserRepository userRepository) {
        this.adminServiceImpl = adminServiceImpl;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void createDefaultAdmin() {
        String defaultAdminUsername = "admin";
        String defaultAdminEmail = "admin@ohbs.com";
        String defaultAdminPassword = "admin123";

        // Check if admin already exists by username or email
        boolean adminExists = userRepository.existsByEmail( defaultAdminEmail) && userRepository.existsByUsername(defaultAdminUsername);
        if (!adminExists) {
            // Build the user registration DTO
            RegisterUserDTO userDTO = RegisterUserDTO.builder()
                    .username(defaultAdminUsername)
                    .email(defaultAdminEmail)
                    .password(defaultAdminPassword)
                    .build();

            // Build the admin request DTO
            AdminRequestDTO adminRequestDTO = AdminRequestDTO.builder()
                    .firstName("Admin")
                    .lastName("Master")
                    .phone("9876543210")
                    .build();

            // Use the adminService to create the admin
            adminServiceImpl.createAdmin(adminRequestDTO, userDTO);
        }
    }
    
    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody RegisterCustomerDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (FieldError error : result.getFieldErrors()) {
                errorMessage.append(String.format("%s: %s ", error.getField(), error.getDefaultMessage()));
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        authService.registerCustomer(registerDTO);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/register/manager")
    public ResponseEntity<String> registerManager(@Valid @RequestBody RegisterManagerDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (FieldError error : result.getFieldErrors()) {
                errorMessage.append(String.format("%s: %s ", error.getField(), error.getDefaultMessage()));
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        authService.registerManager(registerDTO);
        return ResponseEntity.ok("Manager registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginDTO) {
        String token = authService.login(loginDTO);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
