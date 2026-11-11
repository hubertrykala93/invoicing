package com.example.invoicing.contractor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ContractorFilter {
    private final Boolean active;
    private final String search;
}
