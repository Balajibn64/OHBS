package com.ohbs.manager.exception;

public class ManagerAccessDeniedException extends RuntimeException {
    public ManagerAccessDeniedException(Long userId) {
        super("Access denied for manager with user ID: " + userId);
    }
    
}