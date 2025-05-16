package com.ohbs.Customer.service.impl;

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
import com.ohbs.Customer.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomerProfile(CustomerRequestDTO dto, Long userId) {
        // Check if the user exists in the users table
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Look for an existing customer, including soft-deleted ones
        Customer existingCustomer = customerRepository.findByUserId(userId).orElse(null);

        if (existingCustomer != null) {
            if (!existingCustomer.isDeleted()) {
                // Profile already exists and is active
                throw new UserAlreadyHasCustomerProfileException("User already has a customer profile.");
            } else {
                // Restore soft-deleted profile and update the fields
                existingCustomer.setDeleted(false);
                existingCustomer.setFirstName(dto.getFirstName());
                existingCustomer.setLastName(dto.getLastName());
                existingCustomer.setPhone(dto.getPhone());
                existingCustomer.setAddress(dto.getAddress());
                Customer restoredCustomer = customerRepository.save(existingCustomer);
                return mapToResponseDTO(restoredCustomer);
            }
        }

        // Create a new customer profile if one doesn't exist
        Customer customer = Customer.builder()
                .user(user)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .isDeleted(false)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomerProfile(Long userId) {
        // Fetch the customer profile by userId and ensure it's not deleted
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        return mapToResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomerProfile(Long userId, CustomerRequestDTO dto) {
        // Fetch the customer profile to update by userId and ensure it's not deleted
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomerProfile(Long userId) {
        // Fetch the customer profile to delete by userId and ensure it's not deleted
        Customer customer = customerRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer profile not found"));

        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    // Helper method to map the customer entity to a response DTO
    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .email(customer.getUser().getEmail())
                .build();
    }
}
