package com.example.invoicing.reports;

import java.time.Year;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ContractorsReportResponse {
    private Year year;
    private List<ContractorReportResponse> contractors;
}
