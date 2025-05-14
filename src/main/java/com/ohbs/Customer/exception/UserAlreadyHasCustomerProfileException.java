package com.ohbs.Customer.exception;

public class UserAlreadyHasCustomerProfileException extends RuntimeException {
    public UserAlreadyHasCustomerProfileException(String message) {
        super(message);
    }
}
