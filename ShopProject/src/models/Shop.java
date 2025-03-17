package models;

import contracts.Product;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.person.Cashier;
import models.person.Customer;
import models.product.ProductImpl;
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
                int discountThreshold, BigDecimal
                        discountPercentage) {
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
        return inventory;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void addCashier(Cashier cashier) {
        this.cashiers.add(cashier);
    }

    public List<Receipt> getReceipts() {
        return receipts;
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

    public void addStock(Product product, int quantity, BigDecimal deliveryCost) {
        StockItem stockItem = inventory.get(product.getId());
        if (stockItem == null) {
            stockItem = new StockItem(product, quantity, deliveryCost);
            inventory.put(product.getId(), stockItem);
        } else {
            stockItem.increaseQuantity(quantity);
        }
    }

    public Receipt processSale(Cashier cashier, Customer customer, LocalDate currentDate)
            throws InsufficientStockException, InsufficientFundsException, ProductExpiredException {

        if (cashier == null || customer == null || currentDate == null) {
            throw new IllegalArgumentException("Cashier, customer, and date must be provided.");
        }

        if (cashier.getCashDesk() == null) {
            throw new IllegalArgumentException("Cashier must be at a cash desk.");
        }

        Map<Product, Integer> shoppingCart = customer.getShoppingCart();
        List<ReceiptItem> receiptItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            StockItem stockItem = inventory.get(product.getId());

            if (stockItem == null || stockItem.getQuantity() < quantity) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal unitPrice = product.calculateSellingPrice(this, currentDate);
            BigDecimal itemTotal = unitPrice.multiply(new BigDecimal(quantity));
            receiptItems.add(new ReceiptItem(product, quantity, unitPrice, itemTotal));
            totalAmount = totalAmount.add(itemTotal);
        }

        if (customer.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for customer: " + customer.getName());
        }

        customer.deductBalance(totalAmount);

        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            StockItem stockItem = inventory.get(entry.getKey().getId());
            stockItem.decreaseQuantity(entry.getValue());
        }

        Receipt receipt = new Receipt(cashier, currentDate, receiptItems, totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        receipts.add(receipt);

        return receipt;
    }

    public BigDecimal calculateTotalCosts() {
        BigDecimal totalCosts = BigDecimal.ZERO;

        for (Cashier cashier : cashiers) {
            totalCosts = totalCosts.add(cashier.getSalary());
        }

        for (StockItem stockItem : inventory.values()) {
            BigDecimal deliveryCost = stockItem.getDeliveryCost();
            totalCosts = totalCosts.add(deliveryCost);
        }

        return totalCosts;
    }

    public BigDecimal calculateTotalIncome() {
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Receipt receipt : receipts) {
            totalIncome = totalIncome.add(receipt.getTotalAmount());
        }

        return totalIncome;
    }

    public BigDecimal calculateProfit() {
        return calculateTotalIncome().subtract(calculateTotalCosts());
    }

}
