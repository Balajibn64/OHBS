package com.ohbs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ohbs.Customer.exception.CustomerNotFoundException;
import com.ohbs.Customer.exception.InvalidCustomerDataException;
import com.ohbs.Customer.exception.OperationNotAllowedException;
import com.ohbs.Customer.exception.UnauthorizedAccessException;
import com.ohbs.Customer.exception.UserAlreadyHasCustomerProfileException;
import com.ohbs.auth.exception.InvalidCredentialsException;
import com.ohbs.auth.exception.UserAlreadyExistsException;
import com.ohbs.auth.exception.UserNotFoundException;
import com.ohbs.common.dto.ErrorResponse;
import com.ohbs.hotelmgt.exception.DuplicateHotelNameException;
import com.ohbs.hotelmgt.exception.HotelImageUploadException;
import com.ohbs.hotelmgt.exception.InvalidRatingException;
import com.ohbs.hotelmgt.exception.RecordNotFoundException;
import com.ohbs.hotelmgt.exception.ValidationErrorException;
import com.ohbs.room.exception.InvalidRoomDataException;
import com.ohbs.room.exception.RoomAlreadyExistsException;
import com.ohbs.room.exception.RoomNotAvailableException;
import com.ohbs.manager.exception.InvalidManagerUpdateException;
import com.ohbs.manager.exception.ManagerAccessDeniedException;
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
    
//    Hotel Exception
    
    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFound(
            HotelNotFoundException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Hotel not found: " + ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateHotelNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateHotelName(
            DuplicateHotelNameException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Duplicate hotel name: " + ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HotelImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleHotelImageUploadException(
    		HotelImageUploadException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Hotel image upload failed: " + ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(ValidationErrorException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error: " + ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRating(InvalidRatingException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid rating: " + ex.getMessage(),
            req.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
        @ExceptionHandler(RecordNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleRecordNotFoundException(
                RecordNotFoundException ex, HttpServletRequest req) {

            ErrorResponse response = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Record not found: " + ex.getMessage(),
                    req.getRequestURI()
            );

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleJsonParseError(
                HttpMessageNotReadableException ex, HttpServletRequest req) {

            ErrorResponse response = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Malformed JSON: Please check the request format and data types.",
                    req.getRequestURI()
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String message, String path) {
            ErrorResponse response = new ErrorResponse(status.value(), message, path);
            return new ResponseEntity<>(response, status);
        }
        
//       Rooms Exception
        
        @ExceptionHandler(RoomAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleRoomExists(RoomAlreadyExistsException ex, HttpServletRequest request) {
            return new ResponseEntity<>(new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    ex.getMessage(),
                    request.getRequestURI()
            ), HttpStatus.CONFLICT);
        }

        @ExceptionHandler(RoomNotAvailableException.class)
        public ResponseEntity<ErrorResponse> handleRoomUnavailable(RoomNotAvailableException ex, HttpServletRequest request) {
            return new ResponseEntity<>(new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    request.getRequestURI()
            ), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(InvalidRoomDataException.class)
        public ResponseEntity<ErrorResponse> handleInvalidData(InvalidRoomDataException ex, HttpServletRequest request) {
            return new ResponseEntity<>(new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    request.getRequestURI()
            ), HttpStatus.BAD_REQUEST);
        }

}
