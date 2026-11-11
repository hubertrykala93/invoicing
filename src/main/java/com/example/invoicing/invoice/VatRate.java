package com.example.invoicing.invoice;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VatRate {
    VAT_23(BigDecimal.valueOf(0.23), "23%"),
    VAT_8(BigDecimal.valueOf(0.08), "8%"),
    VAT_5(BigDecimal.valueOf(0.05), "5%"),
    VAT_0(BigDecimal.ZERO, "0%"),
    EXEMPT(BigDecimal.ZERO, "zw");

    private final BigDecimal rate;
    private final String label;
}
