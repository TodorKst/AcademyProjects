package services;

import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.Shop;
import models.StockItem;
import models.contracts.Product;
import models.person.Cashier;
import models.person.Customer;
import models.receipt.Receipt;
import models.receipt.ReceiptItem;
import services.contracts.ShopService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopServiceImpl implements ShopService {

    @Override
    public Receipt processSale(Shop shop, Cashier cashier, Customer customer, LocalDate currentDate)
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
            StockItem stockItem = shop.getInventory().get(product.getId());

            if (stockItem == null || stockItem.getQuantity() < quantity) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal unitPrice = product.calculateSellingPrice(shop, currentDate);
            BigDecimal itemTotal = unitPrice.multiply(new BigDecimal(quantity));
            receiptItems.add(new ReceiptItem(product, quantity, unitPrice, itemTotal));
            totalAmount = totalAmount.add(itemTotal);
        }

        if (customer.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for customer: " + customer.getName());
        }

        customer.deductBalance(totalAmount);

        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            StockItem stockItem = shop.getInventory().get(entry.getKey().getId());
            stockItem.decreaseQuantity(entry.getValue());
        }

        Receipt receipt = new Receipt(cashier, currentDate, receiptItems, totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        shop.addReceipt(receipt);

        return receipt;
    }

    @Override
    public BigDecimal calculateTotalCosts(Shop shop) {
        BigDecimal totalCosts = BigDecimal.ZERO;

        for (Cashier cashier : shop.getCashiers()) {
            totalCosts = totalCosts.add(cashier.getSalary());
        }

        for (StockItem stockItem : shop.getInventory().values()) {
            BigDecimal deliveryCost = stockItem.getDeliveryCost();
            totalCosts = totalCosts.add(deliveryCost);
        }

        return totalCosts;
    }

    @Override
    public BigDecimal calculateTotalIncome(Shop shop) {
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Receipt receipt : shop.getReceipts()) {
            totalIncome = totalIncome.add(receipt.getTotalAmount());
        }

        return totalIncome;
    }

    @Override
    public BigDecimal calculateProfit(Shop shop) {
        return calculateTotalIncome(shop).subtract(calculateTotalCosts(shop));
    }

}
