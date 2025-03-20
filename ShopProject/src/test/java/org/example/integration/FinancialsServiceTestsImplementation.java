package org.example.integration;

import org.example.enums.ProductCategory;
import org.example.exceptions.InvalidInputException;
import org.example.models.CashDesk;
import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.person.Cashier;
import org.example.models.product.NonPerishableProduct;
import org.example.models.receipt.Receipt;
import org.example.services.FinancialsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialsServiceTestsImplementation {

    private FinancialsServiceImpl financialsService;
    private Shop shop;
    private CashDesk cashDesk;

    @BeforeEach
    void setUp() {
        financialsService = FinancialsServiceImpl.getInstance();
        shop = new Shop("Test Shop", new BigDecimal("1.2"), new BigDecimal("1.1"), 10, new BigDecimal("5.0"));
        cashDesk = new CashDesk(1);
    }

    @Test
    void calculateTotalCosts_ShouldReturnCorrectValue() {
        Cashier cashier1 = new Cashier("1", "Alice", cashDesk, new BigDecimal("1500"));
        Cashier cashier2 = new Cashier("2", "Bob", cashDesk, new BigDecimal("1800"));
        shop.addCashier(cashier1);
        shop.addCashier(cashier2);

        NonPerishableProduct product1 = new NonPerishableProduct("P1", "Laptop", ProductCategory.NON_FOOD, new BigDecimal("1000"));
        NonPerishableProduct product2 = new NonPerishableProduct("P2", "Phone", ProductCategory.NON_FOOD, new BigDecimal("500"));

        StockItem stockItem1 = new StockItem(product1, 10, new BigDecimal("300"));
        StockItem stockItem2 = new StockItem(product2, 5, new BigDecimal("500"));

        shop.addStock(stockItem1.getProduct(), stockItem1.getQuantity(), stockItem1.getDeliveryCost());
        shop.addStock(stockItem2.getProduct(), stockItem2.getQuantity(), stockItem2.getDeliveryCost());

        BigDecimal expectedTotalCosts = new BigDecimal("4100");

        assertEquals(expectedTotalCosts, financialsService.calculateTotalCosts(shop));
    }

    @Test
    void calculateTotalIncome_ShouldReturnCorrectValue() {
        Receipt receipt1 = new Receipt(new Cashier("1", "Alice", cashDesk, new BigDecimal("1500")),
                LocalDate.now(), Collections.emptyList(), new BigDecimal("5000"));
        Receipt receipt2 = new Receipt(new Cashier("2", "Bob", cashDesk, new BigDecimal("1800")),
                LocalDate.now(), Collections.emptyList(), new BigDecimal("2000"));

        shop.addReceipt(receipt1);
        shop.addReceipt(receipt2);

        BigDecimal expectedTotalIncome = new BigDecimal("7000"); // 5000 + 2000
        assertEquals(expectedTotalIncome, financialsService.calculateTotalIncome(shop));
    }

    @Test
    void calculateProfit_ShouldReturnCorrectValue() {
        Receipt receipt1 = new Receipt(new Cashier("1", "Alice", cashDesk, new BigDecimal("1500")),
                LocalDate.now(), Collections.emptyList(), new BigDecimal("5000"));
        Receipt receipt2 = new Receipt(new Cashier("2", "Bob", cashDesk, new BigDecimal("1800")),
                LocalDate.now(), Collections.emptyList(), new BigDecimal("2000"));
        shop.addReceipt(receipt1);
        shop.addReceipt(receipt2);

        Cashier cashier = new Cashier("3", "Charlie", cashDesk, new BigDecimal("1500"));
        shop.addCashier(cashier);

        NonPerishableProduct product = new NonPerishableProduct("P3", "Monitor", ProductCategory.NON_FOOD, new BigDecimal("800"));
        StockItem stockItem = new StockItem(product, 5, new BigDecimal("500"));
        shop.addStock(stockItem.getProduct(), stockItem.getQuantity(), stockItem.getDeliveryCost());

        BigDecimal expectedProfit = new BigDecimal("5000"); // (5000 + 2000) - (1500 + 500)

        assertEquals(expectedProfit, financialsService.calculateProfit(shop));
    }

    @Test
    void calculateTotalCosts_ShouldThrowException_WhenShopIsInvalid() {
        Shop emptyShop = new Shop();
        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(emptyShop));
    }

    @Test
    void calculateTotalIncome_ShouldThrowException_WhenReceiptsAreEmpty() {
        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalIncome(shop));
    }

    @Test
    void calculateTotalCosts_ShouldReturnZero_WhenInventoryIsEmpty() {
        Cashier cashier = new Cashier("1", "Alice", cashDesk, new BigDecimal("1500"));
        shop.addCashier(cashier);

        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(shop));
    }

    @Test
    void calculateTotalCosts_ShouldThrowException_WhenShopIsNull() {
        Shop nullShop = null;
        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(nullShop));
    }

}
