package com.ohbs.hotelmgt.exception;

public class ValidationErrorException extends RuntimeException {
    public ValidationErrorException(String message) {
        super(message);
    }
}