package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private UUID id;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private String reference;

    public static PaymentResponse from(PaymentEntity paymentEntity) {
        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setId(paymentEntity.getId());
        paymentResponse.setAmount(paymentEntity.getAmount());
        paymentResponse.setPaymentDate(paymentEntity.getPaymentDate());
        paymentResponse.setMethod(paymentEntity.getMethod());
        paymentResponse.setReference(paymentEntity.getReference());

        return paymentResponse;
    }
}
