package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(precision = 12, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal netUnitPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VatRate vatRate;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal netValue;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal vatValue;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal grossValue;

    public BigDecimal calculateNetto(ItemEntity itemEntity) {
        BigDecimal netUnitPrice = itemEntity.getNetUnitPrice();
        BigDecimal quantity = itemEntity.getQuantity();

        return quantity.multiply(netUnitPrice).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateVat(ItemEntity itemEntity) {
        VatRate vatRate = itemEntity.getVatRate();
        BigDecimal netto = calculateNetto(itemEntity);

        return netto.multiply(vatRate.getRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateGross(ItemEntity itemEntity) {
        return calculateNetto(itemEntity).add(calculateVat(itemEntity));
    }

    public ItemEntity copy() {
        ItemEntity copy = new ItemEntity();

        copy.setGrossValue(grossValue);
        copy.setName(name);
        copy.setNetUnitPrice(netUnitPrice);
        copy.setNetValue(netValue);
        copy.setPosition(position);
        copy.setQuantity(quantity);
        copy.setUnit(unit);
        copy.setVatRate(vatRate);
        copy.setVatValue(vatValue);
        copy.setInvoice(invoice);

        return copy;
    }
}
