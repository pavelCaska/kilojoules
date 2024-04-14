package com.pc.kilojoules.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalFoodFormDTO {

    @NotNull(message = "Consumption date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    private String mealType;

    @NotBlank
    private String foodName;

    @NotNull
    @DecimalMin(value = "0.0", message = "Size must be greater than or equal to zero")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0", message = "Size must be greater than or equal to zero")
    private BigDecimal portionSize;

}
