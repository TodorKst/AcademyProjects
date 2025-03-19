package models.receipt;

import models.contracts.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReceiptItem implements Serializable {
//  same as in receipt class, all fields are final because they should not be changed after the receipt item is created to keep an accurate record of the transaction
    private final Product product;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal totalPrice;

    public ReceiptItem(Product product, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}
