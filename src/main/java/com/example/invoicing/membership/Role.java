package com.example.invoicing.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    OWNER("Owner"),
    ACCOUNTANT("Accountant"),
    VIEWER("Viewer");

    private final String label;
}
