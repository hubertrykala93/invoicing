package com.example.invoicing.contractor;

import com.example.invoicing.common.Address;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ContractorRequest {
    @NotBlank(message = "Contractor name is required")
    @Size(min = 2, max = 150, message = "Contractor name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "NIP is required")
    @Pattern(regexp = "\\d{10}", message = "NIP must consist of exactly 10 digits")
    private String nip;

    @NotNull(message = "Address is required")
    @Valid
    private Address address;

    @Email(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Default payment term is required")
    @Min(value = 0, message = "Default payment term must be at least {value} days")
    @Max(value = 90, message = "Default payment term must not exceed {value} days")
    private Integer defaultPaymentTermDays;
}
