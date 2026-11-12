package com.example.invoicing.invoice;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ItemResponse {
    private UUID id;
    private Integer position;
    private String name;
    private BigDecimal quantity;
    private Unit unit;
    private BigDecimal netUnitPrice;
    private VatRate vatRate;
    private BigDecimal netValue;
    private BigDecimal vatValue;
    private BigDecimal grossValue;

    public static ItemResponse from(ItemEntity itemEntity) {
        ItemResponse itemResponse = new ItemResponse();

        itemResponse.setId(itemEntity.getId());
        itemResponse.setPosition(itemEntity.getPosition());
        itemResponse.setName(itemEntity.getName());
        itemResponse.setQuantity(itemEntity.getQuantity());
        itemResponse.setUnit(itemEntity.getUnit());
        itemResponse.setNetUnitPrice(itemEntity.getNetUnitPrice());
        itemResponse.setVatRate(itemEntity.getVatRate());
        itemResponse.setNetValue(itemEntity.getNetValue());
        itemResponse.setVatValue(itemEntity.getVatValue());
        itemResponse.setGrossValue(itemEntity.getGrossValue());

        return itemResponse;
    }
}
