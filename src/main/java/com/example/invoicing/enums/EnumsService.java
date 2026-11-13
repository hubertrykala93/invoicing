package com.example.invoicing.enums;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.invoicing.invoice.PaymentMethod;
import com.example.invoicing.invoice.Status;
import com.example.invoicing.invoice.Unit;
import com.example.invoicing.invoice.VatRate;
import com.example.invoicing.membership.Role;
import com.example.invoicing.user.InvitationStatus;

@Service
public class EnumsService {
    public EnumsResponse getAll() {
        return new EnumsResponse(
            List.of(PaymentMethod.values()).stream().map(p -> new EnumOptionResponse(p.name(), p.getLabel())).toList(),
            List.of(Status.values()).stream().map(s -> new EnumOptionResponse(s.name(), s.getLabel())).toList(),
            List.of(Unit.values()).stream().map(u -> new EnumOptionResponse(u.name(), u.getLabel())).toList(),
            List.of(VatRate.values()).stream().map(v -> new EnumOptionResponse(v.name(), v.getLabel())).toList(),
            List.of(Role.values()).stream().map(r -> new EnumOptionResponse(r.name(), r.getLabel())).toList(),
            List.of(InvitationStatus.values()).stream().map(i -> new EnumOptionResponse(i.name(), i.getLabel())).toList()
        );
    }
}
