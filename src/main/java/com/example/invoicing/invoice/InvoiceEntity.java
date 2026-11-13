package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import com.example.invoicing.contractor.ContractorEntity;
import com.example.invoicing.organization.OrganizationEntity;
import com.example.invoicing.user.UserEntity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "invoice", uniqueConstraints = @UniqueConstraint(columnNames = { "number", "organization_id" }))
@Getter
@Setter
@ToString
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_id")
    private UserEntity issuedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice", orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    @Column(precision = 12, scale = 2)
    private BigDecimal totalNet = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalVat = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalGross = BigDecimal.ZERO;

    @Column(length = 500)
    private String notes;

    @Column(length = 200)
    private String cancelReason;

    public void addItem(ItemEntity itemEntity) {
        itemEntity.setPosition(items.size() + 1);
        itemEntity.setNetValue(itemEntity.calculateNetto(itemEntity));
        itemEntity.setVatValue(itemEntity.calculateVat(itemEntity));
        itemEntity.setGrossValue(itemEntity.calculateGross(itemEntity));
        itemEntity.setInvoice(this);

        items.add(itemEntity);

        calculateTotal();
    }

    public void addPayment(PaymentEntity paymentEntity) {
        payments.add(paymentEntity);

        if (getPaymentsSum().compareTo(getTotalGross()) < 0) {
            status = Status.PARTIALLY_PAID;
        }

        if (getPaymentsSum().compareTo(getTotalGross()) == 0) {
            status = Status.PAID;
        }

        paymentEntity.setInvoice(this);
    }

    public void deletePayment() {
        if (getTotalGross().compareTo(getRemainingAmount()) == 0) {
            status = Status.ISSUED;
        }

        if (getTotalGross().compareTo(getRemainingAmount()) != 0) {
            status = Status.PARTIALLY_PAID;
        }
    }

    public void removeItem(ItemEntity itemEntity) {
        items.remove(itemEntity);

        IntStream.range(0, items.size()).forEach(i -> {
            items.get(i).setPosition(i + 1);
        });

        calculateTotal();
    }

    private void calculateTotal() {
        totalNet = items.stream().map(ItemEntity::getNetValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalVat = items.stream().map(ItemEntity::getVatValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalGross = items.stream().map(ItemEntity::getGrossValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isOverdue() {
        return (status == Status.ISSUED || status == Status.PARTIALLY_PAID) && dueDate.isBefore(LocalDate.now());
    }

    public BigDecimal getRemainingAmount() {
        return totalGross.subtract(getPaymentsSum());
    }

    public BigDecimal getPaymentsSum() {
        return payments.stream().map(PaymentEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void issue(LocalDate date) {
        issueDate = date;
        status = Status.ISSUED;
        dueDate = issueDate.plusDays(paymentTermDays);
    }

    public void cancel() {
        status = Status.CANCELLED;
    }

    public boolean isDraft() {
        return status == Status.DRAFT;
    }

    public boolean isCancelled() {
        return status == Status.CANCELLED;
    }

    public boolean isPayable() {
        return status == Status.ISSUED || status == Status.PARTIALLY_PAID;
    }

    public boolean isIssued() {
        return status == Status.ISSUED || status == Status.PARTIALLY_PAID || status == Status.PAID;
    }

    public boolean isPaid() {
        return status == Status.PAID;
    }
}
