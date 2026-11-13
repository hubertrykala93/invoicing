package com.example.invoicing.reports;

import java.math.BigDecimal;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MonthsResponse {
    private Month month;
    private Integer invoiceCount;
    private BigDecimal net;
    private BigDecimal vat;
    private BigDecimal gross;
}
