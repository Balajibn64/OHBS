package com.ohbs.manager.service;

import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;

import java.util.List;

public interface ManagerService {
    ManagerResponseDTO createManagerProfile(ManagerRequestDTO dto, Long userId);
    ManagerResponseDTO getManagerProfile(Long userId);
    ManagerResponseDTO updateManagerProfile(Long userId, ManagerRequestDTO dto);
    void deleteManagerProfile(Long userId);
    List<ManagerResponseDTO> getAllManagers();
}
