package com.pc.kilojoules.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MealFoodDTO {
    private Long id;
    private Long foodId;
    private String foodName;
    private BigDecimal quantity;
    private BigDecimal adjustedKiloJoules;
    private BigDecimal adjustedProteins;
    private BigDecimal adjustedCarbohydrates;
    private BigDecimal adjustedFiber;
    private BigDecimal adjustedFat;

}