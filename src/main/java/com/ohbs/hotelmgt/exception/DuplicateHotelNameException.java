package com.ohbs.hotelmgt.exception;

public class DuplicateHotelNameException extends RuntimeException {
    public DuplicateHotelNameException(String message) {
        super(message);
    }
}
