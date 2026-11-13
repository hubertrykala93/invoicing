package com.example.invoicing.reports;

import java.time.Year;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportsFilter {
    @PastOrPresent(message = "Report year cannot be in the future")
    private Year year = Year.now();

    @Min(1)
    @Max(12)
    private Integer month;
}
