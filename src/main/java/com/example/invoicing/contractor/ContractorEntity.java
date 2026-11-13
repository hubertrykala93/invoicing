package com.example.invoicing.contractor;

import java.util.UUID;

import com.example.invoicing.common.Address;
import com.example.invoicing.organization.OrganizationEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contractor", uniqueConstraints = @UniqueConstraint(columnNames = { "nip", "organization_id" }))
@Getter
@Setter
@ToString
public class ContractorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 10)
    private String nip;

    @Embedded
    @Column(nullable = false)
    private Address address;

    private String email;

    @Column(nullable = false)
    private Integer defaultPaymentTermDays = 14;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private Boolean active = true;

    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }

    public boolean isActive() {
        return active == true;
    }
}
