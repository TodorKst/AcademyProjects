package implementation;

import org.example.enums.PaperSize;
import org.example.exceptions.InvalidInputException;
import org.example.models.Order;
import org.example.models.PrintingHouse;
import org.example.models.publication.Book;
import org.example.models.publication.Newspaper;
import org.example.services.PrintingHouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrintingHouseServiceImplementationTests {
    private PrintingHouseServiceImpl service;
    private PrintingHouse printingHouse;

    @BeforeEach
    public void setUp() {
        service = (PrintingHouseServiceImpl) PrintingHouseServiceImpl.getInstance();
        service.setMarkupPercentage(new BigDecimal("0.15"));

        printingHouse = new PrintingHouse();
        printingHouse.getOrders().clear();

        Book book = new Book("Sample Book", LocalDate.now(), 100, PaperSize.A4, new BigDecimal("2.00"));
        Order bookOrder = new Order(book, 10);

        Newspaper newspaper = new Newspaper("Daily News", LocalDate.now(), 40, PaperSize.A3);
        Order newspaperOrder = new Order(newspaper, 20);

        printingHouse.getOrders().add(bookOrder);
        printingHouse.getOrders().add(newspaperOrder);
    }

    @Test
    public void calculateTotalExpenses_Should_ReturnTotalPaperCost() {
        BigDecimal expected = new BigDecimal("90.00");
        BigDecimal actual = service.calculateTotalExpenses(printingHouse);
        assertEquals(actual, expected, "Total expenses should be 90");
    }

    @Test
    public void calculateTotalIncome_Should_ReturnTotalIncomeWithMarkup() {
        BigDecimal expected = new BigDecimal("126.50");
        BigDecimal actual = service.calculateTotalIncome(printingHouse);
        assertEquals(expected, actual, "Total income should be 126.50");
    }

    @Test
    public void calculateProfit_Should_ReturnProfit() {
        BigDecimal expected = new BigDecimal("36.50");
        BigDecimal actual = service.calculateProfit(printingHouse);
        assertEquals(expected, actual, "Profit should be 36.50");
    }

    @Test
    public void calculateNetProfit_Should_ReturnNetProfitAfterTax() {
        BigDecimal expected = new BigDecimal("32.85");
        BigDecimal actual = service.calculateNetProfit(printingHouse);
        assertEquals(expected, actual, "Net profit should be 32.85");
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

    @Test
    public void getAndSetMarkupPercentage_Should_WorkProperly() {
        BigDecimal newMarkup = new BigDecimal("0.20");
        service.setMarkupPercentage(newMarkup);
        assertEquals(0, newMarkup.compareTo(service.getMarkupPercentage()), "Markup percentage should be updated to 0.20");
    }
}
