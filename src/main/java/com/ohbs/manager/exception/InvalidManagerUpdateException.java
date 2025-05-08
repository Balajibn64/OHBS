 package com.ohbs.manager.exception;

public class InvalidManagerUpdateException extends RuntimeException {
    public InvalidManagerUpdateException(String reason) {
        super("Invalid manager update: " + reason);
    }
}