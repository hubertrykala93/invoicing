package com.example.invoicing.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    LocalDateTime timestamp, Integer status, ErrorCode code, String message, String path, List<FieldError> fieldErrors
) {

}
