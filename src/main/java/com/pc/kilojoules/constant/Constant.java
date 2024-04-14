package com.pc.kilojoules.constant;

import java.math.BigDecimal;

public class Constant {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    // private constructor to enforce non-instantiability
    private Constant() {
        throw new AssertionError("The class should not be instantiated");
    }
}
