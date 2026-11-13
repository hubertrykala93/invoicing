package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceSummaryResponse {
    private UUID id;
    private String number;
    private Status status;
    private String contractorName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalGross;
    private BigDecimal remainingAmount;
    private boolean overdue;

    public static InvoiceSummaryResponse from(InvoiceEntity invoiceEntity) {
        InvoiceSummaryResponse InvoiceSummaryResponse = new InvoiceSummaryResponse();

        InvoiceSummaryResponse.setId(invoiceEntity.getId());
        InvoiceSummaryResponse.setNumber(invoiceEntity.getNumber());
        InvoiceSummaryResponse.setStatus(invoiceEntity.getStatus());
        InvoiceSummaryResponse.setContractorName(invoiceEntity.getContractor().getName());
        InvoiceSummaryResponse.setIssueDate(invoiceEntity.getIssueDate());
        InvoiceSummaryResponse.setDueDate(invoiceEntity.getDueDate());
        InvoiceSummaryResponse.setTotalGross(invoiceEntity.getTotalGross());
        InvoiceSummaryResponse.setRemainingAmount(invoiceEntity.getRemainingAmount());
        InvoiceSummaryResponse.setOverdue(invoiceEntity.isOverdue());

        return InvoiceSummaryResponse;
    }
}
