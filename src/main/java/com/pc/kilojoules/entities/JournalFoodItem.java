package com.pc.kilojoules.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.pc.kilojoules.constant.Constant.ONE_HUNDRED;

public interface JournalFoodItem {
    BigDecimal getQuantity();
    BigDecimal getKiloJoules();
    BigDecimal getProteins();
    BigDecimal getCarbohydrates();
    BigDecimal getFiber();
    BigDecimal getSugar();
    BigDecimal getFat();
    BigDecimal getSafa();
    BigDecimal getTfa();
    BigDecimal getCholesterol();
    BigDecimal getSodium();
    BigDecimal getCalcium();
    BigDecimal getPhe();

    void setQuantity(BigDecimal quantity);
    void setKiloJoules(BigDecimal kiloJoules);
    void setProteins(BigDecimal proteins);
    void setCarbohydrates(BigDecimal carbohydrates);
    void setFiber(BigDecimal fiber);
    void setSugar(BigDecimal sugar);
    void setFat(BigDecimal fat);
    void setSafa(BigDecimal safa);
    void setTfa(BigDecimal tfa);
    void setCholesterol(BigDecimal cholesterol);
    void setSodium(BigDecimal sodium);
    void setCalcium(BigDecimal calcium);
    void setPhe(BigDecimal phe);

    default void calculateAndUpdate(BigDecimal savedQuantity) {
        this.setKiloJoules(reCalculate(getKiloJoules(), savedQuantity));
        this.setProteins(reCalculate(getProteins(), savedQuantity));
        this.setCarbohydrates(reCalculate(getCarbohydrates(), savedQuantity));
        this.setFiber(reCalculate(getFiber(), savedQuantity));
        this.setSugar(reCalculate(getSugar(), savedQuantity));
        this.setFat(reCalculate(getFat(), savedQuantity));
        this.setSafa(reCalculate(getSafa(), savedQuantity));
        this.setTfa(reCalculate(getTfa(), savedQuantity));
        this.setCholesterol(reCalculate(getCholesterol(), savedQuantity));
        this.setSodium(reCalculate(getSodium(), savedQuantity));
        this.setCalcium(reCalculate(getCalcium(), savedQuantity));
        this.setPhe(reCalculate(getPhe(), savedQuantity));
        this.setQuantity(savedQuantity);
    }

    private BigDecimal reCalculate(BigDecimal source, BigDecimal savedQuantity) {
//        rebase to 100g
        BigDecimal result = source.multiply(ONE_HUNDRED).divide(getQuantity(), RoundingMode.HALF_UP);
//        recalculate to actual quantity
        return result.multiply(savedQuantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP);
    }
}
