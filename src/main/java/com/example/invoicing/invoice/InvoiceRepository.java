package com.example.invoicing.invoice;

import java.time.LocalDate;
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
        AND (CAST(:issueDateFrom as LocalDate) IS NULL OR i.issueDate >= :issueDateFrom)
        AND (CAST(:issueDateTo as LocalDate) IS NULL OR i.issueDate <= :issueDateTo)
        AND (:overdue IS NULL OR (:overdue = true AND i.dueDate < CURRENT_DATE) OR (:overdue = false AND i.dueDate >= CURRENT_DATE))
        """)
    public Page<InvoiceEntity> getAllByFilter(
        Pageable pageable, Status status, UUID contractorId, LocalDate issueDateFrom, LocalDate issueDateTo, Boolean overdue
    );
}
