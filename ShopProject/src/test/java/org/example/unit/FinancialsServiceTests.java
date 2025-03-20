package org.example.unit;

import org.example.exceptions.InvalidInputException;
import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.person.Cashier;
import org.example.models.receipt.Receipt;
import org.example.services.FinancialsServiceImpl;
import org.example.services.contracts.FinancialsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialsServiceTests {

    private FinancialsService financialsService;
    @Mock
    private Shop mockShop;

    @BeforeEach
    void setUp() {
        financialsService = FinancialsServiceImpl.getInstance();
        mockShop = mock(Shop.class);
    }

    @Test
    void calculateTotalCosts_Should_ThrowException_When_ShopIsNull() {
        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(null));
    }

    @Test
    void calculateTotalCosts_Should_ThrowException_When_ShopHasNoCashiersAndNoStockItems() {
        when(mockShop.getCashiers()).thenReturn(Collections.emptyList());
        when(mockShop.getInventory()).thenReturn(Collections.emptyMap());

        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(mockShop));
    }

    @Test
    void calculateTotalCosts_Should_Throw_When_ShopHasCashiersOnly() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getSalary()).thenReturn(new BigDecimal("1000"));

        when(mockShop.getCashiers()).thenReturn(List.of(cashier));
        when(mockShop.getInventory()).thenReturn(Collections.emptyMap());

        Assertions.assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(mockShop));
    }

    @Test
    void calculateTotalCosts_Should_Throw_When_ShopHasNoCashiers() {
        StockItem stockItem = mock(StockItem.class);
        when(stockItem.getDeliveryCost()).thenReturn(new BigDecimal("500"));

        when(mockShop.getCashiers()).thenReturn(Collections.emptyList());
        when(mockShop.getInventory()).thenReturn(Map.of("item1", stockItem));

        Assertions.assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalCosts(mockShop));
    }

    @Test
    void calculateTotalCosts_Should_Calculate_Correctly_When_ShopHasCashiersAndStockItems() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getSalary()).thenReturn(new BigDecimal("1000"));

        StockItem stockItem = mock(StockItem.class);
        when(stockItem.getDeliveryCost()).thenReturn(new BigDecimal("500"));

        when(mockShop.getCashiers()).thenReturn(List.of(cashier));
        when(mockShop.getInventory()).thenReturn(Map.of("item1", stockItem));

        BigDecimal totalCosts = financialsService.calculateTotalCosts(mockShop);

        assertEquals(new BigDecimal("1500"), totalCosts);
    }

    @Test
    void calculateTotalIncome_Should_ThrowException_When_ShopIsNull() {
        assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalIncome(null));
    }

    @Test
    void calculateTotalIncome_Should_Throw_When_ShopHasNoReceipts() {
        when(mockShop.getReceipts()).thenReturn(Collections.emptyList());

        Assertions.assertThrows(InvalidInputException.class, () -> financialsService.calculateTotalIncome(mockShop));
    }

    @Test
    void calculateTotalIncome_Should_Calculate_Correctly_When_ShopHasSingleReceipt() {
        Receipt receipt = mock(Receipt.class);
        when(receipt.getTotalAmount()).thenReturn(new BigDecimal("1200"));

        when(mockShop.getReceipts()).thenReturn(List.of(receipt));

        BigDecimal totalIncome = financialsService.calculateTotalIncome(mockShop);

        assertEquals(new BigDecimal("1200"), totalIncome);
    }

    @Test
    void calculateTotalIncome_Should_Calculate_Correctly_When_ShopHasMultipleReceipts() {
        Receipt receipt1 = mock(Receipt.class);
        when(receipt1.getTotalAmount()).thenReturn(new BigDecimal("1200"));

        Receipt receipt2 = mock(Receipt.class);
        when(receipt2.getTotalAmount()).thenReturn(new BigDecimal("800"));

        when(mockShop.getReceipts()).thenReturn(List.of(receipt1, receipt2));

        BigDecimal totalIncome = financialsService.calculateTotalIncome(mockShop);

        assertEquals(new BigDecimal("2000"), totalIncome);
    }


//    In case of refunds or similar situations in the shop, the method should handle negative receipt values.
    @Test
    void calculateTotalIncome_Should_Handle_NegativeReceiptValues() {
        Receipt receipt1 = mock(Receipt.class);
        when(receipt1.getTotalAmount()).thenReturn(new BigDecimal("-100"));

        Receipt receipt2 = mock(Receipt.class);
        when(receipt2.getTotalAmount()).thenReturn(new BigDecimal("500"));

        when(mockShop.getReceipts()).thenReturn(List.of(receipt1, receipt2));

        BigDecimal totalIncome = financialsService.calculateTotalIncome(mockShop);

        assertEquals(new BigDecimal("400"), totalIncome);
    }

    @Test
    void calculateProfit_Should_ThrowException_When_ShopIsNull() {
        assertThrows(InvalidInputException.class, () -> financialsService.calculateProfit(null));
    }

    @Test
    void calculateProfit_Should_ReturnZero_When_IncomeAndCostsAreZero() {
        FinancialsService spyService = spy(financialsService);

        BigDecimal largeIncome = new BigDecimal("0");
        BigDecimal largeCosts = new BigDecimal("0");

        doReturn(largeIncome).when(spyService).calculateTotalIncome(mockShop);
        doReturn(largeCosts).when(spyService).calculateTotalCosts(mockShop);

        BigDecimal profit = spyService.calculateProfit(mockShop);

        assertEquals(new BigDecimal("0"), profit);
    }

    @Test
    void calculateProfit_Should_CalculateCorrectly_When_IncomeIsHigherThanCosts() {
        FinancialsService spyService = spy(financialsService);

        BigDecimal largeIncome = new BigDecimal("2000");
        BigDecimal largeCosts = new BigDecimal("1000");

        doReturn(largeIncome).when(spyService).calculateTotalIncome(mockShop);
        doReturn(largeCosts).when(spyService).calculateTotalCosts(mockShop);

        BigDecimal profit = spyService.calculateProfit(mockShop);

        assertEquals(new BigDecimal("1000"), profit);
    }

    @Test
    void calculateProfit_Should_CalculateCorrectly_When_IncomeIsLowerThanCosts() {
        FinancialsService spyService = spy(financialsService);

        BigDecimal largeIncome = new BigDecimal("1000");
        BigDecimal largeCosts = new BigDecimal("1500");

        doReturn(largeIncome).when(spyService).calculateTotalIncome(mockShop);
        doReturn(largeCosts).when(spyService).calculateTotalCosts(mockShop);

        BigDecimal profit = spyService.calculateProfit(mockShop);

        assertEquals(new BigDecimal("-500"), profit);
    }

    @Test
    void calculateProfit_Should_Handle_BothNegativeIncomeAndCosts() {
        FinancialsService spyService = spy(financialsService);

        BigDecimal largeIncome = new BigDecimal("-1000");
        BigDecimal largeCosts = new BigDecimal("-500");

        doReturn(largeIncome).when(spyService).calculateTotalIncome(mockShop);
        doReturn(largeCosts).when(spyService).calculateTotalCosts(mockShop);

        BigDecimal profit = spyService.calculateProfit(mockShop);

        assertEquals(new BigDecimal("-500"), profit);
    }

    @Test
    void calculateProfit_Should_Handle_LargeNumbersWithoutOverflow() {
        FinancialsService spyService = spy(financialsService);

        BigDecimal largeIncome = new BigDecimal("999999999999999999");
        BigDecimal largeCosts = new BigDecimal("888888888888888888");

        doReturn(largeIncome).when(spyService).calculateTotalIncome(mockShop);
        doReturn(largeCosts).when(spyService).calculateTotalCosts(mockShop);

        BigDecimal profit = spyService.calculateProfit(mockShop);

        assertEquals(new BigDecimal("111111111111111111"), profit);
    }
    
}
