package com.ohbs.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ohbs.auth.dto.LoginRequestDTO;
import com.ohbs.auth.dto.RegisterCustomerDTO;
import com.ohbs.auth.dto.RegisterManagerDTO;
import com.ohbs.auth.exception.InvalidCredentialsException;
import com.ohbs.auth.exception.UserAlreadyExistsException;
import com.ohbs.auth.exception.UserNotFoundException;
import com.ohbs.common.model.Role;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.security.jwt.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public void registerCustomer(RegisterCustomerDTO registerDTO) {
		if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username is already in use.");
        }
		User user = new User();
		user.setUsername(registerDTO.getUsername());
		user.setEmail(registerDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setRole(Role.CUSTOMER);
		userRepository.save(user);
	}
	
	public void registerManager(RegisterManagerDTO registerDTO) {
		 if (userRepository.existsByEmail(registerDTO.getEmail())) {
	            throw new UserAlreadyExistsException("Email is already in use.");
	        }

	        if (userRepository.existsByUsername(registerDTO.getUsername())) {
	            throw new UserAlreadyExistsException("Username is already in use.");
	        }
		
		User user = new User();
		user.setUsername(registerDTO.getUsername());;
		user.setEmail(registerDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setRole(Role.MANAGER);
		userRepository.save(user);
	}
	
	public String login(LoginRequestDTO loginDTO) {
		User user = userRepository.findByEmailOrUsername(loginDTO.getEmail(), loginDTO.getUsername())
	            .orElseThrow(() -> new UserNotFoundException("Invalid email or username"));

	        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
	            throw new InvalidCredentialsException("Invalid password");
	        }

	        return jwtUtil.generateAccessToken(user);
	}
}
