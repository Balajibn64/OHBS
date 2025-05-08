package com.ohbs.manager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
//    private final HotelRepository hotelRepository;
	private final UserRepository userRepository;

	@Override
	public ManagerResponseDTO createManagerProfile(ManagerRequestDTO dto, Long userId) {

	    try {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new ManagerNotFoundException("User not found"));

	        Manager manager = Manager.builder()
	                .user(user)
	                .firstName(dto.getFirstName())
	                .lastName(dto.getLastName())
	                .phone(dto.getPhone())
	                .build();

	        managerRepository.save(manager);
	        return mapToDto(manager);
	    } catch (Exception e) {
	        throw new ManagerAlreadyExistsException("Manager Already Exists");
	    }
	}


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
//        if (dto.getHotelId() != null) {
//            manager.setHotel(hotelRepository.findById(dto.getHotelId()).orElse(null));
//        }
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
	public List<ManagerResponseDTO> getAllManagers() {
		return managerRepository.findAllByIsDeletedFalse().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private ManagerResponseDTO mapToDto(Manager manager) {
		return ManagerResponseDTO.builder().id(manager.getId()).firstName(manager.getFirstName())
				.lastName(manager.getLastName()).phone(manager.getPhone())
//                .hotelId(manager.getHotel() != null ? manager.getHotel().getId() : null)
//                .hotelName(manager.getHotel() != null ? manager.getHotel().getName() : null)
				.email(manager.getUser().getEmail()).build();
	}
}