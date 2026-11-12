package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.invoicing.contractor.ContractorResponse;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private String number;
    private Status status;
    private ContractorResponse contractor;
    private BuyerSnapshot buyerSnapshot;
    private LocalDate saleDate;
    private LocalDate issueDate;
    private Integer paymentTermDays;
    private List<ItemResponse> items;
    private List<PaymentResponse> payments;
    private LocalDate dueDate;
    private BigDecimal totalNet;
    private BigDecimal totalVat;
    private BigDecimal totalGross;
    private String notes;
    private String cancelReason;

    public static InvoiceResponse from(InvoiceEntity invoiceEntity) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();

        invoiceResponse.setId(invoiceEntity.getId());
        invoiceResponse.setCreatedAt(invoiceEntity.getCreatedAt());
        invoiceResponse.setNumber(invoiceEntity.getNumber());
        invoiceResponse.setStatus(invoiceEntity.getStatus());
        invoiceResponse.setContractor(
            ContractorResponse.from(invoiceEntity.getContractor())
        );
        invoiceResponse.setItems(invoiceEntity.getItems().stream().map(i -> ItemResponse.from(i)).toList());
        invoiceResponse.setPayments(invoiceEntity.getPayments().stream().map(i -> PaymentResponse.from(i)).toList());
        invoiceResponse.setBuyerSnapshot(invoiceEntity.getBuyerSnapshot());
        invoiceResponse.setSaleDate(invoiceEntity.getSaleDate());
        invoiceResponse.setIssueDate(invoiceEntity.getIssueDate());
        invoiceResponse.setPaymentTermDays(invoiceEntity.getPaymentTermDays());
        invoiceResponse.setDueDate(invoiceEntity.getDueDate());
        invoiceResponse.setTotalNet(invoiceEntity.getTotalNet());
        invoiceResponse.setTotalVat(invoiceEntity.getTotalVat());
        invoiceResponse.setTotalGross(invoiceEntity.getTotalGross());
        invoiceResponse.setNotes(invoiceEntity.getNotes());
        invoiceResponse.setCancelReason(invoiceEntity.getCancelReason());

        return invoiceResponse;
    }
}
