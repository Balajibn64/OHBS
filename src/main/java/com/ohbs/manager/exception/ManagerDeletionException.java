package com.ohbs.manager.exception;

public class ManagerDeletionException extends RuntimeException {
    public ManagerDeletionException(String reason) {
        super("Failed to delete manager profile: " + reason);
    }
}