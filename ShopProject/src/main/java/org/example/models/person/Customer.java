package org.example.models.person;


import org.example.exceptions.InvalidInputException;
import org.example.models.contracts.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Customer extends PersonImpl {
    private BigDecimal balance;
    private final Map<Product, Integer> shoppingCart;

    public Customer(String id, String name, BigDecimal balance) {
        super(id, name);
        this.balance = balance;
        this.shoppingCart = new HashMap<>();
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public Map<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void addToShoppingCart(Product product, int quantity) {
        shoppingCart.put(product, shoppingCart.getOrDefault(product, 0) + quantity);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void deductBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Deduction amount must be non-negative.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InvalidInputException("Insufficient balance for customer " + getName());
        }
        this.balance = this.balance.subtract(amount);
    }
}


