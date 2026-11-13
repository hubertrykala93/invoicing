package com.example.invoicing.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvitationStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REVOKED("Revoked");

    private final String label;
}
