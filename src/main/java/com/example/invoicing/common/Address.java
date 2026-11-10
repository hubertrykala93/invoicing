package com.example.invoicing.common;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Embeddable
public record Address(
    @NotBlank(message = "Street is required") @Size(min = 3, max = 100, message = "Street must be between {min} and {max} characters") String street,
    @NotBlank(message = "Postal code is required") @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must match the format 00-000") String postalCode,
    @NotBlank(message = "City is required") @Size(min = 2, max = 50, message = "City must be between {min} and {max} characters") String city,
    @NotBlank(message = "Country is required") String country
) {
    public Address {
        if (country == null || country.isBlank()) {
            country = "Poland";
        }
    }
}
