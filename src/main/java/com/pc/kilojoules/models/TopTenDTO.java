package com.pc.kilojoules.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopTenDTO {
    private Long foodId;
    private Long mealId;

    private String name;
    private BigDecimal quantity;
    private BigDecimal kiloJoules;
    private BigDecimal proteins;
    private BigDecimal carbohydrates;
    private BigDecimal fiber;
    private BigDecimal fat;

    private int count;

}
