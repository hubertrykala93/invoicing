package com.example.invoicing.reports;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VatByMonthResponse {
    private Year year;
    private Month month;
    private List<VatRateResponse> rates;
    private BigDecimal vatToPay;
}
