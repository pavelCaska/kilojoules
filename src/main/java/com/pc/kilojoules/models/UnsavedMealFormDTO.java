package com.pc.kilojoules.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnsavedMealFormDTO {
    @NotNull
    @DecimalMin(value = "0.0", message = "Size must be greater than or equal to zero")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0", message = "Size must be greater than or equal to zero")
    private BigDecimal portionSize;

}
