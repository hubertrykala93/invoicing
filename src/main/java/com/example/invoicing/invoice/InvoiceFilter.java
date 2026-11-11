package com.example.invoicing.invoice;

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
}
