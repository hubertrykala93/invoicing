package com.example.invoicing.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    DRAFT("Draft"),
    ISSUED("Issued"),
    PARTIALLY_PAID("Partially Paid"),
    PAID("Paid"),
    CANCELLED("Cancelled");

    private String label;

    public boolean canTransitionTo(Status target) {
        return switch (this) {
            case DRAFT -> target == ISSUED;
            case ISSUED -> target == PARTIALLY_PAID || target == PAID || target == CANCELLED;
            case PARTIALLY_PAID -> target == ISSUED || target == PAID || target == PARTIALLY_PAID;
            case PAID -> target == ISSUED || target == PARTIALLY_PAID;
            case CANCELLED -> false;
        };
    }
}
