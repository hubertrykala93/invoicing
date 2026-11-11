package com.example.invoicing.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InvoiceCreateRequest {
    @NotNull(message = "Contractor ID is required")
    private UUID contractorId;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    @Size(max = 500, message = "Notes must not exceed {max} characters")
    private String notes;

    @Valid
    private List<ItemCreateRequest> items;

    @Min(value = 0, message = "Payment term must be at least {value} days")
    @Max(value = 90, message = "Payment term must not exceed {value} days")
    private Integer paymentTermDays;
}
