package org.example.models.publication;

import org.example.enums.PaperSize;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Book extends Publication {
    static int numberOfPagesPerSheet = 2;

    private BigDecimal basePrintPrice;

    public Book(String title, LocalDate publishDate, int numberOfPages, PaperSize paperSize, BigDecimal basePrintPrice) {
        super(title, publishDate, numberOfPages, paperSize, numberOfPagesPerSheet);
        this.basePrintPrice = basePrintPrice;
    }

    public BigDecimal getBasePrintPrice() {
        return basePrintPrice;
    }

    public void setBasePrintPrice(BigDecimal basePrintPrice) {
        this.basePrintPrice = basePrintPrice;
    }

    @Override
    public BigDecimal getPrintingCostPerCopy() {
        return getPaperCost().add(basePrintPrice);
    }

    @Override
    public int getNumberOfSheets() {
            return (int) Math.ceil((double) super.getNumberOfPages() / 2);
    }
}