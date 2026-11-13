package com.example.invoicing.reports;

import java.math.BigDecimal;
import java.util.Map;

import com.example.invoicing.invoice.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportsDashboardResponse {
    private Map<Status, Long> invoicesByStatus;
    private BigDecimal unpaidTotal;
    private BigDecimal overdueTotal;
    private Long overdueCount;
    private BigDecimal revenueThisMonthNet;
}
