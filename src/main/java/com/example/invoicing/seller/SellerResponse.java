package com.example.invoicing.seller;

import java.util.UUID;

import com.example.invoicing.common.Address;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SellerResponse {
    private UUID id;
    private String nip;
    private String name;
    private Address address;
    private String bankAccount;

    public static SellerResponse from(SellerEntity sellerEntity) {
        SellerResponse sellerResponse = new SellerResponse();

        sellerResponse.setId(sellerEntity.getId());
        sellerResponse.setNip(sellerEntity.getNip());
        sellerResponse.setName(sellerEntity.getName());
        sellerResponse.setAddress(sellerEntity.getAddress());
        sellerResponse.setBankAccount(sellerEntity.getBankAccount());

        return sellerResponse;
    }
}
