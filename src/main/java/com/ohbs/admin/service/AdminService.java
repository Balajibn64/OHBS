package com.ohbs.admin.service;


import java.util.List;

import com.ohbs.admin.dto.AdminRequestDTO;
import com.ohbs.admin.dto.AdminResponseDTO;

public interface AdminService {
    AdminResponseDTO create(AdminRequestDTO dto, Long userId);
    AdminResponseDTO getById(Long id);
    List<AdminResponseDTO> getAll();
    void delete(Long id);
}
