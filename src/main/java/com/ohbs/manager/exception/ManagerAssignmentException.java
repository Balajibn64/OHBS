package com.ohbs.manager.exception;

public class ManagerAssignmentException extends RuntimeException {
    public ManagerAssignmentException(String reason) {
        super("Failed to assign manager to hotel: " + reason);
    }
}
