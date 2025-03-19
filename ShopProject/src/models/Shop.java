package models;

import models.contracts.Product;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.person.Cashier;
import models.person.Customer;
import models.receipt.Receipt;
import models.receipt.ReceiptItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop {
    private String name;
    private final Map<String, StockItem> inventory = new HashMap<>();
    private final List<Cashier> cashiers;
    private final List<Receipt> receipts;

    private BigDecimal perishableMarkup;
    private BigDecimal nonPerishableMarkup;
    private int discountThreshold;
    private BigDecimal discountPercentage;

    public Shop(String name,
                BigDecimal perishableMarkup,
                BigDecimal nonPerishableMarkup,
                int discountThreshold,
                BigDecimal discountPercentage) {
        this.name = name;
        this.cashiers = new ArrayList<>();
        this.receipts = new ArrayList<>();
        this.perishableMarkup = perishableMarkup;
        this.nonPerishableMarkup = nonPerishableMarkup;
        this.discountThreshold = discountThreshold;
        this.discountPercentage = discountPercentage;
    }

    public Shop() {
        this("", BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, StockItem> getInventory() {
        return new HashMap<>(inventory);
    }

    public void addStock(Product product, int quantity, BigDecimal deliveryCost) {
        StockItem stockItem = inventory.get(product.getId());
        if (stockItem == null) {
            stockItem = new StockItem(product, quantity, deliveryCost);
            inventory.put(product.getId(), stockItem);
        } else {
            stockItem.increaseQuantity(quantity);
        }
    }

    public List<Cashier> getCashiers() {
        return new ArrayList<>(cashiers);
    }

    public void addCashier(Cashier cashier) {
        this.cashiers.add(cashier);
    }

    public List<Receipt> getReceipts() {
        return new ArrayList<>(receipts);
    }

    public void addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
    }

    public BigDecimal getPerishableMarkup() {
        return perishableMarkup;
    }

    public void setPerishableMarkup(BigDecimal perishableMarkup) {
        this.perishableMarkup = perishableMarkup;
    }

    public BigDecimal getNonPerishableMarkup() {
        return nonPerishableMarkup;
    }

    public void setNonPerishableMarkup(BigDecimal nonPerishableMarkup) {
        this.nonPerishableMarkup = nonPerishableMarkup;
    }

    public int getDiscountThreshold() {
        return discountThreshold;
    }

    public void setDiscountThreshold(int discountThreshold) {
        this.discountThreshold = discountThreshold;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }











}
