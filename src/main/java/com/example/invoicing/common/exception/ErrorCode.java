package com.example.invoicing.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;
}
