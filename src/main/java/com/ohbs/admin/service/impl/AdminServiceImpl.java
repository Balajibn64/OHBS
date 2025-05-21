package com.ohbs.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;
import com.ohbs.admin.model.Admin;
import com.ohbs.admin.repository.AdminRepository;
import com.ohbs.admin.service.AdminService;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public AdminResponseDTO create(AdminRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Admin admin = Admin.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .user(user)
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        return mapToResponseDTO(savedAdmin);
    }

    @Override
    public AdminResponseDTO getById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return mapToResponseDTO(admin);
    }

    @Override
    public List<AdminResponseDTO> getAll() {
        return adminRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }

    private AdminResponseDTO mapToResponseDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phone(admin.getPhone())
                .email(admin.getUser().getEmail()) // Assumes User has getEmail()
                .build();
    }
}
