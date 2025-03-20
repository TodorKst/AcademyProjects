package unit;

import org.example.exceptions.InvalidInputException;
import org.example.models.Order;
import org.example.models.PrintingHouse;
import org.example.services.PrintingHouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PrintingHouseServiceUnitTests {
    private PrintingHouseServiceImpl service;

    @Mock
    private PrintingHouse printingHouseMock;

    @Mock
    private Order order1;

    @Mock
    private Order order2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = (PrintingHouseServiceImpl) PrintingHouseServiceImpl.getInstance();
        service.setMarkupPercentage(new BigDecimal("0.15"));
    }

    @Test
    public void calculateTotalExpenses_Should_ReturnTotalPaperCost() {
        when(order1.calculateTotalPaperCost()).thenReturn(new BigDecimal("50.00"));
        when(order2.calculateTotalPaperCost()).thenReturn(new BigDecimal("40.00"));
        List<Order> orders = Arrays.asList(order1, order2);
        when(printingHouseMock.getOrders()).thenReturn(orders);

        BigDecimal result = service.calculateTotalExpenses(printingHouseMock);

        BigDecimal expected = new BigDecimal("90.00");
        assertEquals(expected, result, "Expenses should be 90.00");
    }

    @Test
    public void calculateTotalIncome_Should_ReturnTotalIncomeWithMarkup() {
        when(order1.calculateTotalCost()).thenReturn(new BigDecimal("70.00"));
        when(order2.calculateTotalCost()).thenReturn(new BigDecimal("40.00"));
        List<Order> orders = Arrays.asList(order1, order2);
        when(printingHouseMock.getOrders()).thenReturn(orders);

        BigDecimal result = service.calculateTotalIncome(printingHouseMock);

        BigDecimal expected = new BigDecimal("126.50");
        assertEquals(expected, result, "Income should be 126.50");
    }

    @Test
    public void calculateProfit_Should_ReturnProfit() {
        when(order1.calculateTotalCost()).thenReturn(new BigDecimal("70.00"));
        when(order2.calculateTotalCost()).thenReturn(new BigDecimal("40.00"));
        when(order1.calculateTotalPaperCost()).thenReturn(new BigDecimal("50.00"));
        when(order2.calculateTotalPaperCost()).thenReturn(new BigDecimal("40.00"));
        List<Order> orders = Arrays.asList(order1, order2);
        when(printingHouseMock.getOrders()).thenReturn(orders);

        BigDecimal result = service.calculateProfit(printingHouseMock);

        BigDecimal expected = new BigDecimal("36.50");
        assertEquals(result, expected, "Profit should be 36.50");
    }

    @Test
    public void calculateNetProfit_Should_ReturnNetProfitAfterTax() {
        when(order1.calculateTotalCost()).thenReturn(new BigDecimal("70.00"));
        when(order2.calculateTotalCost()).thenReturn(new BigDecimal("40.00"));
        when(order1.calculateTotalPaperCost()).thenReturn(new BigDecimal("50.00"));
        when(order2.calculateTotalPaperCost()).thenReturn(new BigDecimal("40.00"));
        List<Order> orders = Arrays.asList(order1, order2);
        when(printingHouseMock.getOrders()).thenReturn(orders);

        BigDecimal result = service.calculateNetProfit(printingHouseMock);

        BigDecimal expected = new BigDecimal("32.85");
        assertEquals(result, expected, "Net profit should be 32.85");
    }

    @Test
    public void calculateTotalExpenses_Should_ThrowException_When_PrintingHouseIsNull() {
        assertThrows(InvalidInputException.class, () -> service.calculateTotalExpenses(null));
    }

    @Test
    public void calculateTotalIncome_Should_ThrowException_When_PrintingHouseIsNull() {
        assertThrows(InvalidInputException.class, () -> service.calculateTotalIncome(null));
    }

    @Test
    public void calculateProfit_Should_ThrowException_When_PrintingHouseIsNull() {
        assertThrows(InvalidInputException.class, () -> service.calculateProfit(null));
    }

    @Test
    public void calculateNetProfit_Should_ThrowException_When_PrintingHouseIsNull() {
        assertThrows(InvalidInputException.class, () -> service.calculateNetProfit(null));
    }

    @Test
    public void setMarkupPercentage_Should_ThrowException_When_NegativeValue() {
        assertThrows(InvalidInputException.class, () -> service.setMarkupPercentage(new BigDecimal("-0.05")));
    }

//    Testing both methods at the same time seemed more appropriate than one at a time
//    since they are getters and setters for the same field and dont have any complex logic to test.
    @Test
    public void getAndSetMarkupPercentage_Should_WorkProperly() {
        BigDecimal newMarkup = new BigDecimal("0.20");
        service.setMarkupPercentage(newMarkup);
        assertEquals(0, newMarkup.compareTo(service.getMarkupPercentage()), "Markup percentage should be updated to 0.20");
    }
}
