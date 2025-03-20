package org.example.models;

import org.example.models.publication.Publication;

import java.math.BigDecimal;

public class Order {
    private Publication publication;
    private int copies;

    public Order(Publication publication, int copies) {
        this.publication = publication;
        this.copies = copies;
    }

    public BigDecimal calculateTotalCost() {
        return publication.getPrintingCostPerCopy().multiply(BigDecimal.valueOf(copies));
    }

    public BigDecimal calculateTotalPaperCost() {
        return publication.getPaperCost().multiply(BigDecimal.valueOf(copies));
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}