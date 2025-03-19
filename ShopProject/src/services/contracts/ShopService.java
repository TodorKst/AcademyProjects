package services.contracts;

import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.Shop;
import models.person.Cashier;
import models.person.Customer;
import models.receipt.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ShopService {
    Receipt processSale(Shop shop, Cashier cashier, Customer customer, LocalDate currentDate)
            throws InsufficientStockException, InsufficientFundsException, ProductExpiredException;

    BigDecimal calculateTotalCosts(Shop shop);

    BigDecimal calculateTotalIncome(Shop shop);

    BigDecimal calculateProfit(Shop shop);
}
