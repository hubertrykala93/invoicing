package com.example.invoicing.invoice;

import java.math.BigDecimal;
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
import lombok.AccessLevel;
import lombok.Setter;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

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
}
