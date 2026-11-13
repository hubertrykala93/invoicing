package com.example.invoicing.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.Year;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.invoicing.contractor.ContractorEntity;
import com.example.invoicing.contractor.ContractorRepository;
import com.example.invoicing.invoice.InvoiceEntity;
import com.example.invoicing.invoice.InvoiceRepository;
import com.example.invoicing.invoice.ItemEntity;
import com.example.invoicing.invoice.Status;
import com.example.invoicing.invoice.VatRate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsService {
    private final InvoiceRepository invoiceRepository;
    private final ContractorRepository contractorRepository;

    public ReportsDashboardResponse getReports() {
        List<InvoiceEntity> invoices = invoiceRepository.findAll();

        Map<Status, Long> invoicesByStatus = invoices.stream()
            .collect(
                Collectors.groupingBy(
                    InvoiceEntity::getStatus,
                    () -> new EnumMap<>(Status.class),
                    Collectors.counting()
                )
            );

        BigDecimal unpaidTotal = invoices.stream()
            .filter(InvoiceEntity::isIssued)
            .map(InvoiceEntity::getTotalGross)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal overdueTotal = invoices.stream()
            .filter(i -> i.isIssued() && i.isOverdue())
            .map(InvoiceEntity::getTotalGross)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long overdueCount = invoices.stream().filter(i -> i.isIssued() && i.isOverdue()).count();

        BigDecimal revenueThisMonthNet = invoices.stream()
            .filter(InvoiceEntity::isIssued)
            .map(InvoiceEntity::getTotalNet)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReportsDashboardResponse(
            invoicesByStatus,
            unpaidTotal,
            overdueTotal,
            overdueCount,
            revenueThisMonthNet
        );
    }

    public ReportsByMonthResponse getReportByMonth(ReportsFilter filter) {
        List<InvoiceEntity> invoices = invoiceRepository.findAll()
            .stream()
            .filter(i -> i.isIssued() && Year.of(i.getIssueDate().getYear()).equals(filter.getYear()))
            .toList();

        List<Month> months = List.of(Month.values());

        Map<Month, List<InvoiceEntity>> invoicesByMonth = invoices.stream()
            .collect(
                Collectors.groupingBy(
                    i -> i.getIssueDate().getMonth(),
                    () -> new EnumMap<>(Month.class),
                    Collectors.toList()
                )
            );

        List<MonthsResponse> monthsResponse = months.stream()
            .map(
                m -> {
                    List<InvoiceEntity> monthInvoices = invoicesByMonth.getOrDefault(m, List.of());

                    return new MonthsResponse(
                        m,
                        monthInvoices.size(),
                        monthInvoices.stream().map(InvoiceEntity::getTotalNet).reduce(BigDecimal.ZERO, BigDecimal::add),
                        monthInvoices.stream().map(InvoiceEntity::getTotalVat).reduce(BigDecimal.ZERO, BigDecimal::add),
                        monthInvoices.stream().map(InvoiceEntity::getTotalGross).reduce(BigDecimal.ZERO, BigDecimal::add)
                    );
                }
            )
            .toList();

        MonthsTotalResponse total = new MonthsTotalResponse(
            invoices.size(),
            invoices.stream().map(InvoiceEntity::getTotalNet).reduce(BigDecimal.ZERO, BigDecimal::add),
            invoices.stream().map(InvoiceEntity::getTotalVat).reduce(BigDecimal.ZERO, BigDecimal::add),
            invoices.stream().map(InvoiceEntity::getTotalGross).reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return new ReportsByMonthResponse(filter.getYear(), monthsResponse, total);
    }

    public VatByMonthResponse getVatToPayByMonth(ReportsFilter filter) {
        List<InvoiceEntity> invoices = invoiceRepository.findAll()
            .stream()
            .filter(
                i -> i.isIssued() && Year.of(i.getIssueDate().getYear()).equals(filter.getYear())
                    && i.getIssueDate().getMonthValue() == filter.getMonth()
            )
            .toList();

        List<ItemEntity> items = invoices.stream().flatMap(i -> i.getItems().stream()).toList();

        Map<VatRate, List<ItemEntity>> itemsByVatRate = items.stream()
            .collect(
                Collectors.groupingBy(
                    ItemEntity::getVatRate,
                    () -> new EnumMap<>(VatRate.class),
                    Collectors.toList()
                )
            );

        List<VatRateResponse> rates = List.of(VatRate.values()).stream().map(v -> {
            List<ItemEntity> itemsList = itemsByVatRate.getOrDefault(v, List.of());

            return new VatRateResponse(
                v,
                itemsList.stream().map(ItemEntity::getNetValue).reduce(BigDecimal.ZERO, BigDecimal::add),
                itemsList.stream().map(ItemEntity::getVatValue).reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }).toList();

        BigDecimal vatToPay = invoices.stream().map(InvoiceEntity::getTotalVat).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VatByMonthResponse(filter.getYear(), Month.of(filter.getMonth()), rates, vatToPay);
    }

    public ContractorsReportResponse getReportByContractors(ReportsFilter filter) {
        List<ContractorEntity> contractors = contractorRepository.findAll();
        List<InvoiceEntity> invoices = invoiceRepository.findAll()
            .stream()
            .filter(i -> i.isIssued() && Year.of(i.getIssueDate().getYear()).equals(filter.getYear()))
            .toList();

        BigDecimal totalIncome = invoices.stream().map(InvoiceEntity::getTotalNet).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<UUID, List<InvoiceEntity>> invoicesByContractors = invoices.stream()
            .collect(
                Collectors.groupingBy(
                    i -> i.getContractor().getId(),
                    () -> new HashMap<>(),
                    Collectors.toList()
                )
            );

        List<ContractorReportResponse> contractorsResponse = contractors.stream().map(c -> {
            List<InvoiceEntity> invoiceByContractor = invoicesByContractors.getOrDefault(c.getId(), List.of());

            return new ContractorReportResponse(
                c.getId(),
                c.getName(),
                invoiceByContractor.stream().count(),
                invoiceByContractor.stream().map(InvoiceEntity::getTotalNet).reduce(BigDecimal.ZERO, BigDecimal::add),
                invoiceByContractor.stream()
                    .map(InvoiceEntity::getTotalNet)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalIncome, 2, RoundingMode.HALF_UP),
                invoiceByContractor.stream()
                    .filter(InvoiceEntity::isPaid)
                    .mapToLong(InvoiceEntity::getPaymentTermDays)
                    .average()
                    .orElse(0)
            );
        }).toList();

        return new ContractorsReportResponse(filter.getYear(), contractorsResponse);
    }
}
