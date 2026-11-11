package com.example.invoicing.contractor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractorRepository extends JpaRepository<ContractorEntity, UUID> {

    @Query("""
        SELECT c FROM ContractorEntity c
        WHERE (:active IS NULL OR c.active = :active)
          AND (LOWER(c.name) LIKE CONCAT('%', :search, '%')
               OR c.nip LIKE CONCAT('%', :search, '%'))
        """)
    public Page<ContractorEntity> getAllByFilter(Pageable pageable, Boolean active, String search);

    public boolean existsByNip(String nip);
}
