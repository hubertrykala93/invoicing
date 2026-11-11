package com.example.invoicing.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InvoiceCancelRequest {
    @NotBlank(message = "Cancellation reason is required")
    @Size(min = 5, max = 200, message = "Cancellation reason must be between {min} and {max} characters")
    private String cancelReason;
}
