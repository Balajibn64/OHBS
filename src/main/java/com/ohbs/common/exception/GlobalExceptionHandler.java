package com.ohbs.common.exception;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ohbs.auth.exception.InvalidCredentialsException;
import com.ohbs.auth.exception.UserAlreadyExistsException;
import com.ohbs.auth.exception.UserNotFoundException;
import com.ohbs.common.dto.ErrorResponse;
import com.ohbs.manager.exception.InvalidManagerUpdateException;
import com.ohbs.manager.exception.ManagerAccessDeniedException;
import com.ohbs.manager.exception.ManagerAlreadyAssignedException;
import com.ohbs.manager.exception.ManagerAlreadyExistsException;
import com.ohbs.manager.exception.ManagerAssignmentException;
import com.ohbs.manager.exception.ManagerDeletionException;
import com.ohbs.manager.exception.ManagerHotelMismatchException;
import com.ohbs.manager.exception.ManagerInactiveException;
import com.ohbs.manager.exception.ManagerNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(), ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        StringBuilder errorMessage = new StringBuilder("Validation failed for: ");

        // Loop through all the field errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()));
        }

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage.toString(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(
            InvalidTokenException ex,
            HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<ErrorResponse> handleMissingToken(
    		MissingTokenException ex,
            HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
//    @ExceptionHandler(InvalidTokenException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidTokenException(
//    		InvalidTokenException ex,
//    		HttpServletRequest req) {
//    	ErrorResponse response = new ErrorResponse(
//    			HttpStatus.UNAUTHORIZED.value(),
//    			ex.getMessage(),
//    			req.getRequestURI()
//    		);
//    	return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);		
//    }
    
    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String message, String path) {
        ErrorResponse response = new ErrorResponse(status.value(), message, path);
        return new ResponseEntity<>(response, status);
    }

 // ─────────────────────────────────────────────
    // MANAGER EXCEPTIONS
    // ─────────────────────────────────────────────
    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleManagerNotFound(
            ManagerNotFoundException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleManagerAlreadyExists(
            ManagerAlreadyExistsException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(InvalidManagerUpdateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidManagerUpdate(
            InvalidManagerUpdateException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerDeletionException.class)
    public ResponseEntity<ErrorResponse> handleManagerDeletion(
            ManagerDeletionException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerAssignmentException.class)
    public ResponseEntity<ErrorResponse> handleManagerAssignment(
            ManagerAssignmentException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleManagerAccessDenied(
            ManagerAccessDeniedException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleManagerAlreadyAssigned(
            ManagerAlreadyAssignedException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerHotelMismatchException.class)
    public ResponseEntity<ErrorResponse> handleManagerHotelMismatch(
            ManagerHotelMismatchException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ManagerInactiveException.class)
    public ResponseEntity<ErrorResponse> handleManagerInactive(
            ManagerInactiveException ex, HttpServletRequest req) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI());
    }
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                message,
                path
        );
        return new ResponseEntity<>(error, status);
    }


    
}
