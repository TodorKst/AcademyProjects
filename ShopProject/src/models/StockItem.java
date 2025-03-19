package models;

import models.contracts.Product;
import exceptions.InvalidInputException;

import java.math.BigDecimal;

public class StockItem {
//    product is final because it should not be changed once it is set as if another product is added to the stock, a new StockItem should be created
    private final Product product;
    private int quantity;
    private BigDecimal deliveryCost;

    public StockItem(Product product, int quantity, BigDecimal deliveryCost) {
        this.product = product;
        this.quantity = quantity;
        this.deliveryCost = deliveryCost;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void increaseQuantity() {
        quantity += 1;
    }

    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    public void decreaseQuantity() {
        quantity -= 1;
    }

    public void decreaseQuantity(int amount) {
        if (quantity < amount) {
            throw new InvalidInputException("Insufficient quantity in stock.");
        }
        quantity -= amount;
    }


}
