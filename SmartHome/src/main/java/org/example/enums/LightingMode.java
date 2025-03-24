package org.example.enums;

import java.math.BigDecimal;

public enum LightingMode {
    DIM(BigDecimal.valueOf(0.5)),
    MEDIUM(BigDecimal.valueOf(0.75)),
    STRONG(BigDecimal.valueOf(1.0));


    private final BigDecimal lumenOutputFactor;

    LightingMode(BigDecimal lumenOutputFactor) {
        this.lumenOutputFactor = lumenOutputFactor;
    }

    public BigDecimal getLumenOutputFactor() {
        return lumenOutputFactor;
    }

}
