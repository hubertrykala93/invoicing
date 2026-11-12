package com.example.invoicing.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVOICE_NOT_FOUND(HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVOICE_NOT_DELETABLE(HttpStatus.CONFLICT),
    ISSUE_DEADLINE_EXCEEDED(HttpStatus.CONFLICT),
    SALE_DATE_AFTER_ISSUE_DATE(HttpStatus.CONFLICT),
    INVOICE_EMPTY(HttpStatus.CONFLICT),
    INVALID_STATUS_TRANSITION(HttpStatus.CONFLICT),
    INVOICE_HAS_PAYMENTS(HttpStatus.CONFLICT),
    CONTRACTOR_NOT_FOUND(HttpStatus.NOT_FOUND),
    CONTRACTOR_ALREADY_DEACTIVATED(HttpStatus.CONFLICT),
    CONTRACTOR_ALREADY_ACTIVATED(HttpStatus.CONFLICT),
    INVOICE_NOT_EDITABLE(HttpStatus.CONFLICT),
    DUPLICATE_NIP(HttpStatus.CONFLICT),
    CONTRACTOR_INACTIVE(HttpStatus.CONFLICT);

    private final HttpStatus status;
}
