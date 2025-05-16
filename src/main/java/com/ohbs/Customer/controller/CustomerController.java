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
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    // Create customer profile
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CustomerRequestDTO dto) {

        Long userId = extractUserIdFromToken(token);
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
    public ResponseEntity<String> delete(
            @RequestHeader("Authorization") String token) {

        Long userId = extractUserIdFromToken(token);
        customerService.deleteCustomerProfile(userId);

        // Return a message confirming the successful deletion of the customer profile
        return ResponseEntity.status(HttpStatus.OK).body("Customer profile successfully deleted.");
    }


    private Long extractUserIdFromToken(String token) {
        return jwtUtil.extractUserId(token);
    }
}
