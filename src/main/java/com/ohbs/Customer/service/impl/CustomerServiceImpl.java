package com.ohbs.Customer.service.impl;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.exception.CustomerNotFoundException;
import com.ohbs.Customer.exception.UserAlreadyHasCustomerProfileException;
import com.ohbs.Customer.exception.UserNotFoundException;
import com.ohbs.Customer.model.Customer;
import com.ohbs.Customer.repository.CustomerRepository;
import com.ohbs.Customer.service.CustomerService;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.security.jwt.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final JwtUtil jwtUtil;

//    @Override
//    @Transactional
//    public CustomerResponseDTO createCustomerProfile(CustomerRequestDTO dto, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        Customer existingCustomer = customerRepository.findByUserId(userId).orElse(null);
//
//        if (existingCustomer != null) {
//            if (!existingCustomer.isDeleted()) {
//                throw new UserAlreadyHasCustomerProfileException("User already has a customer profile.");
//            } else {
//                existingCustomer.setDeleted(false);
//                existingCustomer.setFirstName(dto.getFirstName());
//                existingCustomer.setLastName(dto.getLastName());
//                existingCustomer.setPhone(dto.getPhone());
//                existingCustomer.setAddress(dto.getAddress());
//                existingCustomer.setDob(dto.getDob());
//                existingCustomer.setGender(dto.getGender());
//                Customer restoredCustomer = customerRepository.save(existingCustomer);
//                return mapToResponseDTO(restoredCustomer);
//            }
//        }
//
//        Customer customer = Customer.builder()
//                .user(user)
//                .firstName(dto.getFirstName())
//                .lastName(dto.getLastName())
//                .phone(dto.getPhone())
//                .address(dto.getAddress())
//                .dob(dto.getDob())
//                .gender(dto.getGender())
//                .isDeleted(false)
//                .build();
//
//        Customer savedCustomer = customerRepository.save(customer);
//        return mapToResponseDTO(savedCustomer);
//    }

    @Override
    public CustomerResponseDTO getCustomerProfile(Long userId) {
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));
        return mapToResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomerProfile(Long userId, CustomerRequestDTO dto) {
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setDob(dto.getDob());
        customer.setGender(dto.getGender());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomerProfile(Long userId) {
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    @Override
    public void uploadProfileImage(Long userId, MultipartFile imageFile) {
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            customer.setProfileImageUrl(imageUrl);
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .email(customer.getUser().getEmail())
                .profileImageUrl(customer.getProfileImageUrl())
                .dob(customer.getDob())
                .gender(customer.getGender())
                .build();
    }
}
