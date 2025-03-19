package org.example.services.contracts;

import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InsufficientStockException;
import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;
import org.example.models.person.Cashier;
import org.example.models.person.Customer;
import org.example.models.receipt.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ShopService {
    Receipt processSale(Shop shop, Cashier cashier, Customer customer, LocalDate currentDate)
            throws InsufficientStockException, InsufficientFundsException, ProductExpiredException;

    BigDecimal calculateTotalCosts(Shop shop);

    BigDecimal calculateTotalIncome(Shop shop);

    BigDecimal calculateProfit(Shop shop);
}
