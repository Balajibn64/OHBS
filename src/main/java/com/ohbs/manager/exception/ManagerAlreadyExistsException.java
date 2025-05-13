package com.ohbs.manager.exception;

public class ManagerAlreadyExistsException extends RuntimeException {
    public ManagerAlreadyExistsException(Long userId) {
        super("Manager profile already exists for user ID: " + userId);
    }

    public ManagerAlreadyExistsException(String message) {
        super(message);
    }
}