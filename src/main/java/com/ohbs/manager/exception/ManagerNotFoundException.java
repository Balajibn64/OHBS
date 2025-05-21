package com.ohbs.manager.exception;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(Long userId) {
        super("Manager profile not found for user ID: " + userId);
    }

    public ManagerNotFoundException(String message) {
        super(message);
    }
}