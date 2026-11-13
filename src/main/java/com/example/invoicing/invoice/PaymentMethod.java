package com.example.invoicing.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    TRANSFER("Transfer"),
    CASH("Cash"),
    CARD("Card");

    private String label;
}
