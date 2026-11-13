package com.example.invoicing.enums;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnumsResponse {
    private List<EnumOptionResponse> paymentMethods;
    private List<EnumOptionResponse> invoiceStatuses;
    private List<EnumOptionResponse> units;
    private List<EnumOptionResponse> vatRates;
    private List<EnumOptionResponse> roles;
    private List<EnumOptionResponse> invitationStatuses;
}
