package models;

import enums.ProductCategory;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.person.Cashier;
import models.person.Customer;
import models.product.NonPerishableProduct;
import models.product.PerishableProduct;
import models.receipt.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ShopTests {
    private Shop shop;
    private Cashier cashier;
    private Customer customer;
    private PerishableProduct milk;
    private NonPerishableProduct soap;
    private CashDesk cashDesk;

    @BeforeEach
    void setUp() {
        shop = new Shop(
                "Test Shop",
                new BigDecimal("1.20"),
                new BigDecimal("1.30"),
                5,
                new BigDecimal("0.10")
        );

        cashDesk = new CashDesk(1);
        cashier = new Cashier("C001", "John Doe", cashDesk, new BigDecimal("1000.00"));
        customer = new Customer("CU001", "Alice", new BigDecimal("50.00"));

        milk = new PerishableProduct("P001", "Milk", ProductCategory.FOOD, new BigDecimal("2.00"), LocalDate.now().plusDays(10));
        soap = new NonPerishableProduct("NP001", "Soap", ProductCategory.NON_FOOD, new BigDecimal("1.50"));

        shop.addStock(milk, 10, BigDecimal.valueOf(2));  // Cost price: 2
        shop.addStock(soap, 20, BigDecimal.valueOf(1));  // Cost price: 1

        shop.addCashier(cashier);
    }

    @Test
    void processSale_Should_Process_Sale() throws InsufficientFundsException, InsufficientStockException, ProductExpiredException {
        customer.addToShoppingCart(milk, 2);  // 2 Milk @ 2.00 each
        customer.addToShoppingCart(soap, 3);  // 3 Soap @ 1.50 each

        Receipt receipt = shop.processSale(cashier, customer, LocalDate.now());

        assertNotNull(receipt);
        assertEquals(new BigDecimal("10.65"), receipt.getTotalAmount());
        assertEquals(new BigDecimal("39.35"), customer.getBalance());
    }

    @Test
    void processSale_Should_Throw_If_Not_Enough_Balance() {
        customer.setBalance(new BigDecimal("3.00")); // Not enough for purchase
        customer.addToShoppingCart(milk, 2);

        assertThrows(InsufficientFundsException.class, () -> {
            shop.processSale(cashier, customer, LocalDate.now());
        });
    }

    @Test
    void processSale_Should_Throw_If_Not_Enough_Stock() {
        customer.addToShoppingCart(milk, 20); // Trying to buy 20, but only 10 in stock

        assertThrows(InsufficientStockException.class, () -> {
            shop.processSale(cashier, customer, LocalDate.now());
        });
    }

    @Test
    void processSale_Should_Throw_If_Product_Expired() {
        PerishableProduct expiredMilk = new PerishableProduct("P002", "Expired Milk", ProductCategory.FOOD, new BigDecimal("2.00"), LocalDate.now().minusDays(1));
        shop.addStock(expiredMilk, 5, BigDecimal.valueOf(2));

        customer.addToShoppingCart(expiredMilk, 1);

        assertThrows(ProductExpiredException.class, () -> {
            shop.processSale(cashier, customer, LocalDate.now());
        });
    }

    @Test
    void calculateProfit_Should_Calculate_Profit() throws InsufficientFundsException, InsufficientStockException, ProductExpiredException {
        customer.addToShoppingCart(milk, 2);
        customer.addToShoppingCart(soap, 3);
        shop.processSale(cashier, customer, LocalDate.now());

        BigDecimal expectedProfit = shop.calculateTotalIncome().subtract(shop.calculateTotalCosts());
        assertEquals(expectedProfit, shop.calculateProfit());
    }

    @Test
    void processSale_Should_Throw_If_Cashier_Not_In_Shop() {
        Cashier fakeCashier = new Cashier("C999", "Fake Cashier", null, new BigDecimal("1000.00"));
        customer.addToShoppingCart(milk, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            shop.processSale(fakeCashier, customer, LocalDate.now());
        });
    }

    @Test
    void processSale_Should_Handle_Zero_Quantity_Sale() throws InsufficientFundsException, InsufficientStockException, ProductExpiredException {
        Receipt receipt = shop.processSale(cashier, customer, LocalDate.now());

        assertNotNull(receipt);
        assertEquals(BigDecimal.ZERO.setScale(2), receipt.getTotalAmount());
        assertEquals(new BigDecimal("50.00"), customer.getBalance());
    }

    @Test
    void addStock_Should_Increase_Existing_Product_Stock() {
        int initialStock = shop.getInventory().get(milk.getId()).getQuantity();
        shop.addStock(milk, 5, BigDecimal.valueOf(2));

        assertEquals(initialStock + 5, shop.getInventory().get(milk.getId()).getQuantity());
    }

    @Test
    void calculateTotalCosts_Should_Include_Cashier_Salaries() {
        BigDecimal totalCosts = shop.calculateTotalCosts();
        assertTrue(totalCosts.compareTo(cashier.getSalary()) >= 0);
    }

    @Test
    void calculateTotalIncome_Should_Be_Zero_Initially() {
        assertEquals(BigDecimal.ZERO.setScale(0), shop.calculateTotalIncome());
    }

    @Test
    void calculateProfit_Should_Be_Negative_If_No_Sales() {
        assertTrue(shop.calculateProfit().compareTo(BigDecimal.ZERO) < 0);
    }

}
