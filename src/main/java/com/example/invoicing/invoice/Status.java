package com.example.invoicing.invoice;

public enum Status {
    DRAFT,
    ISSUED,
    PARTIALLY_PAID,
    PAID,
    CANCELLED;

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
