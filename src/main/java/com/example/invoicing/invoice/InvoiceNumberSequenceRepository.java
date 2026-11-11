package com.example.invoicing.invoice;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceNumberSequenceRepository extends JpaRepository<InvoiceNumberSequenceEntity, UUID> {
    public Optional<InvoiceNumberSequenceEntity> findByMonthAndYear(int month, int year);
}
