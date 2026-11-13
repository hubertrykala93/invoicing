package com.example.invoicing.organization;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.invoicing.common.Address;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Setter;

@Entity
@Table(name = "organization")
public class OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true)
    private String nip;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false, unique = true, length = 26)
    private String bankAccount;
}
