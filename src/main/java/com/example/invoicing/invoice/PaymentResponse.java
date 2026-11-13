package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.invoicing.user.UserEntity;
import com.example.invoicing.user.UserSummaryResponse;

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
    private UserSummaryResponse recordedBy;

    public static PaymentResponse from(PaymentEntity paymentEntity) {
        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setId(paymentEntity.getId());
        paymentResponse.setAmount(paymentEntity.getAmount());
        paymentResponse.setPaymentDate(paymentEntity.getPaymentDate());
        paymentResponse.setMethod(paymentEntity.getMethod());
        paymentResponse.setReference(paymentEntity.getReference());

        UserEntity recordedBy = paymentEntity.getRecordedBy();

        paymentResponse.setRecordedBy(recordedBy != null ? UserSummaryResponse.from(recordedBy) : null);

        return paymentResponse;
    }
}
