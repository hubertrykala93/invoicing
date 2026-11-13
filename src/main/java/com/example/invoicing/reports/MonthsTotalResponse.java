package com.example.invoicing.reports;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MonthsTotalResponse {
    private Integer invoiceCount;
    private BigDecimal net;
    private BigDecimal vat;
    private BigDecimal gross;
}
