package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.invoicing.contractor.ContractorEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Setter;

@Entity
@Table(name = "invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Status status = Status.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    private ContractorEntity contractor;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "buyer_name"))
    @AttributeOverride(name = "nip", column = @Column(name = "buyer_nip"))
    @AttributeOverride(name = "address.street", column = @Column(name = "buyer_street"))
    @AttributeOverride(name = "address.postalCode", column = @Column(name = "buyer_postal_code"))
    @AttributeOverride(name = "address.city", column = @Column(name = "buyer_city"))
    @AttributeOverride(name = "address.country", column = @Column(name = "buyer_country"))
    private BuyerSnapshot buyerSnapshot;

    @Column(nullable = false)
    private LocalDate saleDate;

    private LocalDate issueDate;

    private Integer paymentTermDays;

    private LocalDate dueDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice", orphanRemoval = true)
    @OrderBy(value = "position")
    private List<ItemEntity> items = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice", orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    @Column(precision = 12, scale = 2)
    private BigDecimal totalNet;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalVat;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalGross;

    @Column(length = 500)
    private String notes;
}
