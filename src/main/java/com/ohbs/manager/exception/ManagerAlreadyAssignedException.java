package com.ohbs.manager.exception;

public class ManagerAlreadyAssignedException extends RuntimeException {
    public ManagerAlreadyAssignedException(Long managerId) {
        super("Manager with ID " + managerId + " is already assigned to a hotel.");
    }
}