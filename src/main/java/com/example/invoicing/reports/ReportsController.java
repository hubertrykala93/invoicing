package com.example.invoicing.reports;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {
    private final ReportsService reportsService;

    @GetMapping("/dashboard")
    @ResponseStatus(HttpStatus.OK)
    public ReportsDashboardResponse getReports() {
        return reportsService.getReports();
    }

    @GetMapping("/monthly")
    @ResponseStatus(HttpStatus.OK)
    public ReportsByMonthResponse getReportByMonth(@ModelAttribute @Valid ReportsFilter filter) {
        return reportsService.getReportByMonth(filter);
    }

    @GetMapping("/vat")
    @ResponseStatus(HttpStatus.OK)
    public VatByMonthResponse getVatToPayByMonth(@ModelAttribute @Valid ReportsFilter filter) {
        return reportsService.getVatToPayByMonth(filter);
    }

    @GetMapping("/contractors")
    @ResponseStatus(HttpStatus.OK)
    public ContractorsReportResponse getReportByContractors(@ModelAttribute @Valid ReportsFilter filter) {
        return reportsService.getReportByContractors(filter);
    }
}
