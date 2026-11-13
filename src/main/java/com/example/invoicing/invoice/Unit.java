package com.example.invoicing.invoice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Unit {
    PCS("pcs"),
    HOUR("hour"),
    SERVICE("service");

    private final String label;
}
