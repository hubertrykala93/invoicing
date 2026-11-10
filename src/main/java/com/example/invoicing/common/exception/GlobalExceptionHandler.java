package com.example.invoicing.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldError> fieldErrors = exception.getFieldErrors()
            .stream()
            .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
            .toList();

        return buildResponse(ErrorCode.VALIDATION_ERROR, "Validation failed", request, fieldErrors);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception, HttpServletRequest request) {
        return buildResponse(exception.getCode(), exception.getMessage(), request, null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
        ErrorCode code, String message, HttpServletRequest request, List<FieldError> fieldErrors
    ) {
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(), code.getStatus().value(), code, message, request.getRequestURI(), fieldErrors
        );

        return ResponseEntity.status(code.getStatus()).body(response);
    }
}
