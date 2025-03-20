package org.example.services;


import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InsufficientStockException;
import org.example.exceptions.InvalidInputException;
import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.contracts.Product;
import org.example.models.person.Cashier;
import org.example.models.person.Customer;
import org.example.models.receipt.Receipt;
import org.example.models.receipt.ReceiptItem;
import org.example.services.contracts.ShopService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopServiceImpl implements ShopService {

    private static ShopServiceImpl instance;

    private ShopServiceImpl() {
    }

    public static ShopServiceImpl getInstance() {
        synchronized (ShopServiceImpl.class) {
            if (instance == null) {
                instance = new ShopServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public Receipt processSale(Shop shop, Cashier cashier, Customer customer, LocalDate currentDate)
            throws InsufficientStockException, InsufficientFundsException, ProductExpiredException {
        throwIfAnyInputValueIsNull(shop, cashier, customer, currentDate);
        throwIfCashierIsntAtCashDesk(cashier);

        Map<Product, Integer> shoppingCart = customer.getShoppingCart();
        List<ReceiptItem> receiptItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            StockItem stockItem = shop.getInventory().get(product.getId());

            throwIfInsufficientStock(stockItem, quantity, product);

            BigDecimal unitPrice = product.calculateSellingPrice(shop, currentDate);
            BigDecimal itemTotal = unitPrice.multiply(new BigDecimal(quantity));
            receiptItems.add(new ReceiptItem(product, quantity, unitPrice, itemTotal));
            totalAmount = totalAmount.add(itemTotal);
        }

        throwIfCustomerHasInsufficientBalance(customer, totalAmount);

        customer.deductBalance(totalAmount);

        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            StockItem stockItem = shop.getInventory().get(entry.getKey().getId());
            stockItem.decreaseQuantity(entry.getValue());
        }

        Receipt receipt = new Receipt(cashier, currentDate, receiptItems, totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        shop.addReceipt(receipt);

        return receipt;
    }

    private static void throwIfCashierIsntAtCashDesk(Cashier cashier) {
        if (cashier.getCashDesk() == null) {
            throw new InvalidInputException("Cashier must be at a cash desk.");
        }
    }

    private static void throwIfAnyInputValueIsNull(Shop shop, Cashier cashier, Customer customer, LocalDate currentDate) {
        if (shop == null ||cashier == null || customer == null || currentDate == null) {
            throw new InvalidInputException("Shop, cashier, customer, and date must be provided.");
        }
    }

    private static void throwIfInsufficientStock(StockItem stockItem, int quantity, Product product) {
        if (stockItem == null || stockItem.getQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }
    }

    private static void throwIfCustomerHasInsufficientBalance(Customer customer, BigDecimal totalAmount) {
        if (customer.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for customer: " + customer.getName());
        }
    }

}
