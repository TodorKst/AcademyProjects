package org.example.unit;

import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InsufficientStockException;
import org.example.exceptions.ProductExpiredException;
import org.example.models.CashDesk;
import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.contracts.Product;
import org.example.models.person.Cashier;
import org.example.models.person.Customer;
import org.example.models.receipt.Receipt;
import org.example.services.ShopServiceImpl;
import org.example.services.contracts.FinancialsService;
import org.example.services.contracts.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTests {

    private ShopService shopService;
    @Mock
    private Shop shop;
    @Mock
    private Cashier cashier;
    @Mock
    private Customer customer;
    @Mock
    private Product product;
    @Mock
    private StockItem stockItem;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        shopService = ShopServiceImpl.getInstance();
    }

    @Test
    void processSale_Should_ThrowException_When_CashierOrCustomerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> shopService.processSale(shop, null, customer, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> shopService.processSale(shop, cashier, null, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_CashierHasNoCashDesk() {
        when(cashier.getCashDesk()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_InsufficientStock() {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 5);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(new HashMap<>());
        when(product.getId()).thenReturn("P123");

        assertThrows(InsufficientStockException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_InsufficientFunds() throws Exception {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 1);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(Map.of("P123", stockItem));
        when(product.getId()).thenReturn("P123");
        when(stockItem.getQuantity()).thenReturn(10);
        when(product.calculateSellingPrice(shop, LocalDate.now())).thenReturn(BigDecimal.valueOf(100));
        when(customer.getBalance()).thenReturn(BigDecimal.valueOf(50));

        assertThrows(InsufficientFundsException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_DeductStockAndReturnReceipt_When_Successful() throws Exception {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 2);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(Map.of("P123", stockItem));
        when(product.getId()).thenReturn("P123");
        when(stockItem.getQuantity()).thenReturn(10);
        when(product.calculateSellingPrice(shop, LocalDate.now())).thenReturn(BigDecimal.valueOf(50));
        when(customer.getBalance()).thenReturn(BigDecimal.valueOf(200));
        when(cashier.getCashDesk()).thenReturn(mock(CashDesk.class));

        doNothing().when(customer).deductBalance(any());

        Receipt receipt = shopService.processSale(shop, cashier, customer, LocalDate.now());

        assertNotNull(receipt);
        assertEquals(100, receipt.getTotalAmount().intValue());
        verify(customer).deductBalance(BigDecimal.valueOf(100));
        verify(stockItem).decreaseQuantity(2);
    }

//    @Test
//    void calculateTotalCosts_Should_ReturnCorrectTotalCosts() {
//        Cashier cashier1 = mock(Cashier.class);
//        Cashier cashier2 = mock(Cashier.class);
//        StockItem item1 = mock(StockItem.class);
//        StockItem item2 = mock(StockItem.class);
//
//        when(cashier1.getSalary()).thenReturn(BigDecimal.valueOf(1000));
//        when(cashier2.getSalary()).thenReturn(BigDecimal.valueOf(1200));
//        when(item1.getDeliveryCost()).thenReturn(BigDecimal.valueOf(500));
//        when(item2.getDeliveryCost()).thenReturn(BigDecimal.valueOf(700));
//
//        when(shop.getCashiers()).thenReturn(java.util.List.of(cashier1, cashier2));
//        when(shop.getInventory()).thenReturn(Map.of("P1", item1, "P2", item2));
//
//        BigDecimal totalCosts = financialsService.calculateTotalCosts(shop);
//        assertEquals(BigDecimal.valueOf(3400), totalCosts);
//    }
//
//    @Test
//    void calculateTotalIncome_Should_ReturnCorrectTotalIncome() {
//        Receipt receipt1 = mock(Receipt.class);
//        Receipt receipt2 = mock(Receipt.class);
//
//        when(receipt1.getTotalAmount()).thenReturn(BigDecimal.valueOf(1500));
//        when(receipt2.getTotalAmount()).thenReturn(BigDecimal.valueOf(2500));
//
//        when(shop.getReceipts()).thenReturn(java.util.List.of(receipt1, receipt2));
//
//        BigDecimal totalIncome = financialsService.calculateTotalIncome(shop);
//        assertEquals(BigDecimal.valueOf(4000), totalIncome);
//    }
//
//    @Test
//    void calculateProfit_Should_ReturnCorrectProfit() {
//        ShopService spyShopService = spy(shopService);
//
//        doReturn(BigDecimal.valueOf(5000)).when(spyShopService).calculateTotalIncome(shop);
//        doReturn(BigDecimal.valueOf(3000)).when(spyShopService).calculateTotalCosts(shop);
//
//        BigDecimal profit = spyShopService.calculateProfit(shop);
//        assertEquals(BigDecimal.valueOf(2000), profit);
//    }
}