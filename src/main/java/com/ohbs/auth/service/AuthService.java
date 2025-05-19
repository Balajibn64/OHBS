package com.ohbs.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ohbs.auth.dto.LoginRequestDTO;
import com.ohbs.auth.dto.RegisterCustomerDTO;
import com.ohbs.auth.dto.RegisterManagerDTO;
import com.ohbs.auth.dto.RegisterUserDTO;
import com.ohbs.auth.exception.InvalidCredentialsException;
import com.ohbs.auth.exception.UserAlreadyExistsException;
import com.ohbs.auth.exception.UserNotFoundException;
import com.ohbs.common.model.Role;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.manager.exception.ManagerNotFoundException;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.repository.ManagerRepository;
import com.ohbs.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final ManagerRepository managerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	// Generic registration method for AdminService
	public User registerUser(RegisterUserDTO dto, Role role) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new UserAlreadyExistsException("Email is already in use.");
		}
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new UserAlreadyExistsException("Username is already in use.");
		}

		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(role);

		return userRepository.save(user);
	}

	// Original login logic remains the same
	public String login(LoginRequestDTO loginDTO) {
		User user = userRepository.findByEmailOrUsername(loginDTO.getEmail(), loginDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("Invalid email or username"));

		if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid password");
		}

		return jwtUtil.generateAccessToken(user);
	}

	// Optional: keep existing methods if you need them elsewhere
	public void registerCustomer(RegisterCustomerDTO registerDTO) {
		registerUser(RegisterUserDTO.builder()
						.username(registerDTO.getUsername())
						.email(registerDTO.getEmail())
						.password(registerDTO.getPassword())
						.build(),
				Role.CUSTOMER);
	}

	public void registerManager(RegisterManagerDTO registerDTO) {
		registerUser(RegisterUserDTO.builder()
						.username(registerDTO.getUsername())
						.email(registerDTO.getEmail())
						.password(registerDTO.getPassword())
						.build(),
				Role.MANAGER);
	}
	
	public Manager getManagerById(Long id) {
	    return managerRepository.findByIdAndIsDeletedFalse(id)
	            .orElseThrow(() -> new ManagerNotFoundException("Manager with ID " + id + " not found or is deleted."));
	}
}
