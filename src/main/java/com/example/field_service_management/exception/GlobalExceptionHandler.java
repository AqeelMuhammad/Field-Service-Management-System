package com.example.field_service_management.exception;

import com.example.field_service_management.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomApiException(CustomApiException ex) {
        String errorMessage = ex.getMessage();
        HttpStatus status = ex.getStatus();

        ApiResponse<String> response = new ApiResponse<>("Fail", errorMessage, null);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        String errorMessageString = String.join(", ", errorMessages);

        ApiResponse<String> response = new ApiResponse<>("Fail", errorMessageString, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String errorMessage = "Unsupported Media Type: " + ex.getMessage();

        ApiResponse<String> response = new ApiResponse<>("Fail", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeExceptions(RuntimeException ex) {
        String errorMessage = "An error occurred: " + ex.getMessage();

        ApiResponse<String> response = new ApiResponse<>("Fail", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex, WebRequest request) {
        String errorMessage = "Internal Server Error: " + ex.getMessage();

        ApiResponse<String> response = new ApiResponse<>("Fail", errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
