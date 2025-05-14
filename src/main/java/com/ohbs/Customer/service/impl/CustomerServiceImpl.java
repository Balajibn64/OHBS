package com.ohbs.Customer.service.impl;

import com.ohbs.Customer.dto.CustomerRequestDTO;
import com.ohbs.Customer.dto.CustomerResponseDTO;
import com.ohbs.Customer.model.Customer;
import com.ohbs.Customer.exception.CustomerNotFoundException;
import com.ohbs.Customer.exception.UserAlreadyHasCustomerProfileException;
import com.ohbs.Customer.exception.UserNotFoundException;
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
    private final JwtUtil JwtUtil;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomerProfile(CustomerRequestDTO dto, Long userId) {
        if (customerRepository.findByUserIdAndIsDeletedFalse(userId).isPresent()) {
            throw new UserAlreadyHasCustomerProfileException("User already has a customer profile.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Customer customer = Customer.builder()
       //         .user(user)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

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

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
  //              .email(customer.getUser().getEmail())
                .build();
    }
}
