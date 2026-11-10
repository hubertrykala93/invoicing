package com.example.invoicing.seller;

import com.example.invoicing.common.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SellerUpdateRequest {
    @NotBlank(message = "Seller name is required")
    @Size(min = 2, max = 150, message = "Seller name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "NIP is required")
    private String nip;

    @NotNull(message = "Address is required")
    private Address address;

    @NotBlank(message = "Bank account number is required")
    @Size(min = 26, max = 26, message = "Bank account number must contain exactly {max} digits")
    private String bankAccount;
}
