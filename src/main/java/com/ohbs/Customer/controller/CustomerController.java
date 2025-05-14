package com.ohbs.Customer.controller;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.service.CustomerService;
import com.ohbs.security.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("customerProfileController")
@RequestMapping("/customers")
@RequiredArgsConstructor // Automatically inject the dependencies through constructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;  // Inject CustomerService
    private final JwtUtil jwtUtil;  // Inject JwtUtil

    // Create customer profile
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CustomerRequestDTO dto) {

        Long userId = extractUserIdFromToken(token);  // Extract user ID from token
        return ResponseEntity.ok(customerService.createCustomerProfile(dto, userId));
    }

    // Get customer profile
    @GetMapping
    public ResponseEntity<CustomerResponseDTO> get(
            @RequestHeader("Authorization") String token) {

        Long userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(customerService.getCustomerProfile(userId));
    }

    // Update customer profile
    @PutMapping
    public ResponseEntity<CustomerResponseDTO> update(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CustomerRequestDTO dto) {

        Long userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(customerService.updateCustomerProfile(userId, dto));
    }

    // Delete customer profile
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String token) {

        Long userId = extractUserIdFromToken(token);
        customerService.deleteCustomerProfile(userId);
        return ResponseEntity.noContent().build();
    }

    // Helper method to extract the user ID from the JWT token
    private Long extractUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " prefix
            return jwtUtil.getUserIdFromEmail(token);  // Extract user ID from the token
        } else {
            throw new IllegalArgumentException("Invalid token format");
        }
    }
}
