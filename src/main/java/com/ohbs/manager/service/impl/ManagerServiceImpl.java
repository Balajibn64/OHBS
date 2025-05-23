package com.ohbs.manager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.manager.dto.ManagerRequestDTO;
import com.ohbs.manager.dto.ManagerResponseDTO;
import com.ohbs.manager.exception.ManagerAlreadyExistsException;
import com.ohbs.manager.exception.ManagerNotFoundException;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.repository.ManagerRepository;
import com.ohbs.manager.service.ManagerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

//    @Override
//    public ManagerResponseDTO createManagerProfile(ManagerRequestDTO dto, Long userId) {
//        try {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new ManagerNotFoundException("User not found"));
//
//            Manager existingManager = managerRepository.findByUserId(userId).orElse(null);
//
//            if (existingManager != null) {
//                if (!existingManager.isDeleted()) {
//                    throw new ManagerAlreadyExistsException("Manager profile already exists");
//                } else {
//                    // Restore soft-deleted profile
//                    existingManager.setDeleted(false);
//                    existingManager.setFirstName(dto.getFirstName());
//                    existingManager.setLastName(dto.getLastName());
//                    existingManager.setPhone(dto.getPhone());
//                    return mapToDto(managerRepository.save(existingManager));
//                }
//            }
//
//            Manager manager = Manager.builder()
//                    .user(user)
//                    .firstName(dto.getFirstName())
//                    .lastName(dto.getLastName())
//                    .phone(dto.getPhone())
//                    .isDeleted(false)
//                    .build();
//
//            return mapToDto(managerRepository.save(manager));
//        } catch (Exception e) {
//            throw new ManagerAlreadyExistsException("Manager creation failed");
//        }
//    }

    @Override
    public ManagerResponseDTO getManagerProfile(Long userId) {
        Manager manager = managerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));
        return mapToDto(manager);
    }

    @Override
    public ManagerResponseDTO updateManagerProfile(Long userId, ManagerRequestDTO dto) {
        Manager manager = managerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        manager.setFirstName(dto.getFirstName());
        manager.setLastName(dto.getLastName());
        manager.setPhone(dto.getPhone());

        return mapToDto(managerRepository.save(manager));
    }

    @Override
    public void deleteManagerProfile(Long userId) {
        Manager manager = managerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        manager.setDeleted(true);
        managerRepository.save(manager);
    }

    @Override
    public void uploadProfileImage(Long userId, MultipartFile imageFile) {
        Manager manager = managerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager profile not found"));

        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            manager.setProfileImageUrl(imageUrl);
            managerRepository.save(manager);
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    // ✅ Helper method to convert Manager to ManagerResponseDTO
    private ManagerResponseDTO mapToDto(Manager manager) {
        return ManagerResponseDTO.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phone(manager.getPhone())
                .email(manager.getUser().getEmail())
                .profileImageUrl(manager.getProfileImageUrl())
                .build();
    }
}
