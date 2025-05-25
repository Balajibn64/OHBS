package com.ohbs.manager.controller;

import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.service.ManagerService;
import com.ohbs.security.jwt.JwtUtil;
import com.ohbs.common.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> createManagerProfile(
            @Valid @RequestBody ManagerRequestDTO dto,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        return ResponseEntity.ok(managerService.createManagerProfile(dto, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> getManagerProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        return ResponseEntity.ok(managerService.getManagerProfile(userId));
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ManagerResponseDTO> updateManagerProfile(
            @Valid @RequestBody ManagerRequestDTO dto,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        return ResponseEntity.ok(managerService.updateManagerProfile(userId, dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteManagerProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        managerService.deleteManagerProfile(userId);
        return ResponseEntity.ok("Manager profile successfully deleted.");
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> uploadManagerImage(
            @RequestParam("image") MultipartFile imageFile,
            HttpServletRequest request) {

        Long userId = extractUserId(request);
        managerService.uploadProfileImage(userId, imageFile);
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
