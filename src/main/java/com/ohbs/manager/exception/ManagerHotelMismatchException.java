package com.ohbs.manager.exception;

public class ManagerHotelMismatchException extends RuntimeException {
    public ManagerHotelMismatchException(Long managerId, Long hotelId) {
        super("Manager ID " + managerId + " does not belong to Hotel ID " + hotelId);
    }
}
