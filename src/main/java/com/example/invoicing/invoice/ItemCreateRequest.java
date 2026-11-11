package com.example.invoicing.invoice;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateRequest {
    @NotBlank(message = "Item name is required")
    @Size(min = 2, max = 200, message = "Item name must be between {min} and {max} characters")
    private String name;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @Digits(integer = 9, fraction = 3, message = "Quantity can have at most {integer} digits before and {fraction} after the decimal point")
    private BigDecimal quantity;

    @NotNull(message = "Unit is required")
    private Unit unit;

    @NotNull(message = "Net unit price is required")
    @Positive(message = "Net unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Net unit price can have at most {integer} digits before and {fraction} after the decimal point")
    private BigDecimal netUnitPrice;

    @NotNull(message = "VAT rate is required")
    private VatRate vatRate;

    public ItemEntity toEntity() {
        ItemEntity itemEntity = new ItemEntity();

        itemEntity.setName(name);
        itemEntity.setQuantity(quantity);
        itemEntity.setUnit(unit);
        itemEntity.setNetUnitPrice(netUnitPrice);
        itemEntity.setVatRate(vatRate);

        return itemEntity;
    }
}
