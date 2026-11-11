package com.example.invoicing.contractor;

import java.util.UUID;

import com.example.invoicing.common.Address;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ContractorResponse {
    private UUID id;
    private String name;
    private String nip;
    private Address address;
    private String email;
    private Integer defaultPaymentTermDays;
    private boolean active;

    public static ContractorResponse from(ContractorEntity contractorEntity) {
        ContractorResponse contractorResponse = new ContractorResponse();

        contractorResponse.setId(contractorEntity.getId());
        contractorResponse.setName(contractorEntity.getName());
        contractorResponse.setNip(contractorEntity.getNip());
        contractorResponse.setAddress(contractorEntity.getAddress());
        contractorResponse.setEmail(contractorEntity.getEmail());
        contractorResponse.setDefaultPaymentTermDays(contractorEntity.getDefaultPaymentTermDays());
        contractorResponse.setActive(contractorEntity.getActive());

        return contractorResponse;
    }
}
