package com.ohbs.manager.exception;

public class ManagerInactiveException extends RuntimeException {
    public ManagerInactiveException(Long userId) {
        super("Manager profile is inactive or soft-deleted for user ID: " + userId);
    }
}
