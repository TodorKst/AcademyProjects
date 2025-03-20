package org.example.models.publication;

import org.example.enums.PaperSize;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Newspaper extends Publication {
    static int numberOfPagesPerSheet = 4;

    public Newspaper(String title, LocalDate publishDate, int numberOfPages, PaperSize paperSize) {
        super(title, publishDate, numberOfPages, paperSize, numberOfPagesPerSheet);
    }

    @Override
    public BigDecimal getPrintingCostPerCopy() {
        return getPaperCost();
    }

    @Override
    public int getNumberOfSheets() {
            return (int) Math.ceil((double) super.getNumberOfPages() / numberOfPagesPerSheet);
    }
}
