package com.example.invoicing.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND),
    CONTRACTOR_NOT_FOUND(HttpStatus.NOT_FOUND),
    CONTRACTOR_ALREADY_DEACTIVATED(HttpStatus.CONFLICT),
    CONTRACTOR_ALREADY_ACTIVATED(HttpStatus.CONFLICT),
    DUPLICATE_NIP(HttpStatus.CONFLICT);

    private final HttpStatus status;
}
