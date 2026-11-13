package com.example.invoicing.reports;

import java.math.BigDecimal;

import com.example.invoicing.invoice.VatRate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VatRateResponse {
    private VatRate vatRate;
    private BigDecimal net;
    private BigDecimal vat;
}
