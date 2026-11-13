package com.example.invoicing.reports;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ContractorReportResponse {
    private UUID contractorId;
    private String name;
    private Long invoiceCount;
    private BigDecimal revenueNet;
    private BigDecimal revenueShare;
    private Double averagePaymentDays;
}
