package org.example.unit;

import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InsufficientStockException;
import org.example.exceptions.InvalidInputException;
import org.example.models.CashDesk;
import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.contracts.Product;
import org.example.models.person.Cashier;
import org.example.models.person.Customer;
import org.example.models.receipt.Receipt;
import org.example.services.ShopServiceImpl;
import org.example.services.contracts.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
        assertThrows(InvalidInputException.class, () -> shopService.processSale(shop, null, customer, LocalDate.now()));
        assertThrows(InvalidInputException.class, () -> shopService.processSale(shop, cashier, null, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_ShopIsNull() {
        assertThrows(InvalidInputException.class, () -> shopService.processSale(null, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_CashierHasNoCashDesk() {
        when(cashier.getCashDesk()).thenReturn(null);
        assertThrows(InvalidInputException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_DateIsNull() {
        assertThrows(InvalidInputException.class, () -> shopService.processSale(shop, cashier, customer, null));
    }

    @Test
    void processSale_Should_ThrowException_When_NoStock() {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 5);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(new HashMap<>());
        when(product.getId()).thenReturn("P123");
        when(cashier.getCashDesk()).thenReturn(mock(CashDesk.class));

        assertThrows(InsufficientStockException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_NotEnoughStock() {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 5);
        Map<String, StockItem> inventory = new HashMap<>();
        inventory.put("P123", stockItem);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(inventory);
        when(stockItem.getQuantity()).thenReturn(3);
        when(product.getId()).thenReturn("P123");
        when(cashier.getCashDesk()).thenReturn(mock(CashDesk.class));

        assertThrows(InsufficientStockException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_ThrowException_When_InsufficientFunds() {
        Map<Product, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put(product, 1);

        when(customer.getShoppingCart()).thenReturn(shoppingCart);
        when(shop.getInventory()).thenReturn(Map.of("P123", stockItem));
        when(product.getId()).thenReturn("P123");
        when(stockItem.getQuantity()).thenReturn(10);
        when(product.calculateSellingPrice(shop, LocalDate.now())).thenReturn(BigDecimal.valueOf(100));
        when(customer.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(cashier.getCashDesk()).thenReturn(mock(CashDesk.class));

        assertThrows(InsufficientFundsException.class, () -> shopService.processSale(shop, cashier, customer, LocalDate.now()));
    }

    @Test
    void processSale_Should_DeductStockAndReturnReceipt_When_Successful() {
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

}