package com.pc.kilojoules.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalTotalsDTO {

    private BigDecimal totalQuantity = BigDecimal.ZERO;
    private BigDecimal totalKiloJoules = BigDecimal.ZERO;
    private BigDecimal totalProteins = BigDecimal.ZERO;
    private BigDecimal totalCarbohydrates = BigDecimal.ZERO;
    private BigDecimal totalFiber = BigDecimal.ZERO;
    private BigDecimal totalFat = BigDecimal.ZERO;
    private BigDecimal avgQuantity = BigDecimal.ZERO;
    private BigDecimal avgKiloJoules = BigDecimal.ZERO;
    private BigDecimal avgProteins = BigDecimal.ZERO;
    private BigDecimal avgCarbohydrates = BigDecimal.ZERO;
    private BigDecimal avgFiber = BigDecimal.ZERO;
    private BigDecimal avgFat = BigDecimal.ZERO;
}
