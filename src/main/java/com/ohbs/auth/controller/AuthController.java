package com.ohbs.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohbs.auth.dto.LoginRequestDTO;
import com.ohbs.auth.dto.RegisterCustomerDTO;
import com.ohbs.auth.dto.RegisterManagerDTO;
import com.ohbs.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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
