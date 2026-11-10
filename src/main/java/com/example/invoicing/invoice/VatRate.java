package com.example.invoicing.invoice;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VatRate {
    VAT_23(BigDecimal.valueOf(23), "23%"),
    VAT_8(BigDecimal.valueOf(8), "8%"),
    VAT_5(BigDecimal.valueOf(5), "5%"),
    VAT_0(BigDecimal.ZERO, "0%"),
    EXEMPT(BigDecimal.ZERO, "zw");

    private final BigDecimal rate;
    private final String label;
}
