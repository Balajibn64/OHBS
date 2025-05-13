package com.ohbs.common.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    // Constructor to initialize the error response
    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();  // Automatically sets the timestamp to the current time
        this.path = path;
    }
}
