package com.example.invoicing.invoice;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InvoiceFilter {
    private Status status;
    private UUID contractorId;
    private LocalDate issueDateFrom;
    private LocalDate issueDateTo;
    private Boolean overdue;
}
