package org.example.models.publication;

import org.example.enums.PaperSize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Publication {

    private int numberOfPagesPerSheet;
    private final String id;
    private String title;
    private LocalDate publishDate;
    private int numberOfPages;
    private PaperSize paperSize;

    public Publication(String title,
                       LocalDate publishDate,
                       int numberOfPages,
                       PaperSize paperSize,
                       int numberOfPagesPerSheet) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.publishDate = publishDate;
        this.numberOfPages = numberOfPages;
        this.paperSize = paperSize;
        this.numberOfPagesPerSheet = numberOfPagesPerSheet;
    }

    public abstract BigDecimal getPrintingCostPerCopy();

    public abstract int getNumberOfSheets();

    public BigDecimal getPaperCost() {
        return paperSize.getPricePerSheet().multiply(BigDecimal.valueOf(getNumberOfSheets()));
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public PaperSize getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        this.paperSize = paperSize;
    }

    public int getNumberOfPagesPerSheet() {
        return numberOfPagesPerSheet;
    }

    public void setNumberOfPagesPerSheet(int numberOfPagesPerSheet) {
        this.numberOfPagesPerSheet = numberOfPagesPerSheet;
    }
}