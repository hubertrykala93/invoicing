package com.example.invoicing.contractor;

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
@Table(name = "contractor")
public class ContractorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String nip;

    @Embedded
    @Column(nullable = false)
    private Address address;

    private String email;

    @Column(nullable = false)
    private Integer defaultPaymentTermDays = 14;

    @Column(nullable = false)
    private Boolean active = true;

}
