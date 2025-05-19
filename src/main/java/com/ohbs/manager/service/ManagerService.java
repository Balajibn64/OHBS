package com.ohbs.manager.service;

import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ManagerService {
    ManagerResponseDTO createManagerProfile(ManagerRequestDTO dto, Long userId);
    ManagerResponseDTO getManagerProfile(Long userId);
    ManagerResponseDTO updateManagerProfile(Long userId, ManagerRequestDTO dto);
    void uploadProfileImage(Long userId, MultipartFile imageFile);
    void deleteManagerProfile(Long userId);
}
