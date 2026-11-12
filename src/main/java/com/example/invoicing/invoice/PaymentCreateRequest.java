package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentCreateRequest {
    @NotNull(message = "Payment amount is required")
    @Digits(integer = 10, fraction = 2, message = "Payment amount must have at most {integer} integer digits and {fraction} decimal places")
    @Positive(message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @Size(max = 100, message = "Payment reference must not exceed {max} characters")
    private String reference;

    public PaymentEntity toEntity() {
        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setAmount(amount);
        paymentEntity.setMethod(method);
        paymentEntity.setPaymentDate(paymentDate);
        paymentEntity.setReference(reference);

        return paymentEntity;
    }
}
