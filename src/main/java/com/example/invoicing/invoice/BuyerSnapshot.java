package com.example.invoicing.invoice;

import com.example.invoicing.common.Address;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public record BuyerSnapshot(
    @NotBlank(message = "Buyer name is required") String name,
    @NotBlank(message = "Buyer NIP is required") @Size(min = 10, max = 10, message = "Buyer NIP must be exactly {max} characters") String nip,
    @NotNull(message = "Buyer address is required") Address address
) {

}
