package com.example.invoicing.invoice;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "invoice_number_sequence", uniqueConstraints = @UniqueConstraint(columnNames = { "year", "month" }))
@Getter
@Setter
@ToString
public class InvoiceNumberSequenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int lastNumber;

    protected InvoiceNumberSequenceEntity() {
    }

    protected InvoiceNumberSequenceEntity(int month, int year, int lastNumber) {
        this.month = month;
        this.year = year;
        this.lastNumber = lastNumber;
    }
}
