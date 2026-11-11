package com.example.invoicing.invoice;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
    @Query("""
        SELECT i FROM InvoiceEntity i
        WHERE (:status IS NULL OR i.status = :status)
        AND (:contractorId IS NULL OR i.contractor.id = :contractorId)
        """)
    public Page<InvoiceEntity> getAllByFilter(Pageable pageable, Status status, UUID contractorId);
}
