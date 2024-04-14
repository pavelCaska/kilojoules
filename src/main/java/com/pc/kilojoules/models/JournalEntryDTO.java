package com.pc.kilojoules.models;

import com.pc.kilojoules.entities.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class JournalEntryDTO {
    private Long id;
    private Long foodId;
    private Long mealId;
    private MealType mealType;

    private String name;
    private BigDecimal quantity;
    private BigDecimal kiloJoules;
    private BigDecimal proteins;
    private BigDecimal carbohydrates;
    private BigDecimal fiber;
    private BigDecimal fat;
}
