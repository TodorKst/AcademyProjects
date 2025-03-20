package org.example.models.receipt;


import org.example.models.person.Cashier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class Receipt implements Serializable {

    //    all fields are final, as the receipt serves as a record and should not be modified
    private final String receiptId;
    private final Cashier cashier;
    private final LocalDate purchaseDate;
    private final List<ReceiptItem> items;
    private final BigDecimal totalAmount;

    public Receipt(Cashier cashier, LocalDate purchaseDate, List<ReceiptItem> items, BigDecimal totalAmount) {
//        the substring method is there to shorten the uuid so the file name and content is more readable
        this.receiptId = UUID.randomUUID().toString().substring(0, 8);
        this.cashier = cashier;
        this.purchaseDate = purchaseDate;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

}
