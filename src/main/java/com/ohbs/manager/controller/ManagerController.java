package com.ohbs.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.ohbs.common.exception.InvalidTokenException;
import com.ohbs.common.exception.UnauthorizedException;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.service.ManagerService;
import com.ohbs.security.jwt.JwtUtil; // ✅ correct package

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final JwtUtil jwtUtil; // ✅ follow Java naming convention

    // Create manager profile
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> createManagerProfile(@RequestBody ManagerRequestDTO dto,
                                                                   HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        ManagerResponseDTO response = managerService.createManagerProfile(dto, userId);
        return ResponseEntity.ok(response);
    }

    // Get manager profile
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> getManagerProfile(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        ManagerResponseDTO response = managerService.getManagerProfile(userId);
        return ResponseEntity.ok(response);
    }

    // Update manager profile
    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> updateManagerProfile(@RequestBody ManagerRequestDTO dto,
                                                                   HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        ManagerResponseDTO response = managerService.updateManagerProfile(userId, dto);
        return ResponseEntity.ok(response);
    }

    // Soft delete manager profile
    @DeleteMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteManagerProfile(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        managerService.deleteManagerProfile(userId);
        return ResponseEntity.noContent().build();
    }

    // Optional: Admin-only - List all managers
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ManagerResponseDTO>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    // ✅ Correct utility method to extract userId from JWT token
    
    private Long getUserIdFromRequest(HttpServletRequest request) {
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
