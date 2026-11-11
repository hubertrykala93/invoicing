package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceMinimalResponse {
    private UUID id;
    private String number;
    private Status status;
    private String contractorName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalGross;
    private BigDecimal remainingAmount;
    private boolean overdue;

    public static InvoiceMinimalResponse from(InvoiceEntity invoiceEntity) {
        InvoiceMinimalResponse invoiceMinimalResponse = new InvoiceMinimalResponse();

        invoiceMinimalResponse.setId(invoiceEntity.getId());
        invoiceMinimalResponse.setNumber(invoiceEntity.getNumber());
        invoiceMinimalResponse.setStatus(invoiceEntity.getStatus());
        invoiceMinimalResponse.setContractorName(invoiceEntity.getContractor().getName());
        invoiceMinimalResponse.setIssueDate(invoiceEntity.getIssueDate());
        invoiceMinimalResponse.setDueDate(invoiceEntity.getDueDate());
        invoiceMinimalResponse.setTotalGross(invoiceEntity.getTotalGross());
        invoiceMinimalResponse.setRemainingAmount(invoiceEntity.getRemainingAmount());
        invoiceMinimalResponse.setOverdue(invoiceEntity.isOverdue());

        return invoiceMinimalResponse;
    }
}
