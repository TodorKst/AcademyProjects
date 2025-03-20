package org.example.enums;

import java.math.BigDecimal;

public enum PaperSize {
    A1(new BigDecimal("0.80")),
    A2(new BigDecimal("0.40")),
    A3(new BigDecimal("0.20")),
    A4(new BigDecimal("0.10")),
    A5(new BigDecimal("0.05"));

    private BigDecimal pricePerSheet;

    PaperSize(BigDecimal pricePerSheet) {
        this.pricePerSheet = pricePerSheet;
    }

    public BigDecimal getPricePerSheet() {
        return pricePerSheet;
    }

    public void setPricePerSheet(BigDecimal pricePerSheet) {
        this.pricePerSheet = pricePerSheet;
    }
}