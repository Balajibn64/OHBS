package com.ohbs.Customer.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.service.CustomerService;
import com.ohbs.common.exception.UnauthorizedException;
import com.ohbs.security.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> createCustomerProfile(
            @Valid @RequestBody CustomerRequestDTO dto,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        return ResponseEntity.ok(customerService.createCustomerProfile(dto, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getCustomerProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        return ResponseEntity.ok(customerService.getCustomerProfile(userId));
    }

    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> updateCustomerProfile(
            @Valid @RequestBody CustomerRequestDTO dto,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        return ResponseEntity.ok(customerService.updateCustomerProfile(userId, dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteCustomerProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        customerService.deleteCustomerProfile(userId);
        return ResponseEntity.ok("Customer profile successfully deleted.");
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("image") MultipartFile imageFile,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        customerService.uploadProfileImage(userId, imageFile);
        return ResponseEntity.ok("Profile image uploaded successfully.");
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
}
