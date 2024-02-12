package com.pc.kilojoules.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MealDTO {
    private String mealName;
    private Long mealId;
    private List<MealFoodDTO> mealFoodsDTO;

    private BigDecimal sumQuantity;
    private BigDecimal sumAdjustedKiloJoules;
    private BigDecimal sumAdjustedProteins;
    private BigDecimal sumAdjustedCarbohydrates;
    private BigDecimal sumAdjustedFiber;
    private BigDecimal sumAdjustedFat;

}