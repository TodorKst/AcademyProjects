package unit;

import org.example.exceptions.InvalidInputException;
import org.example.models.PrintingHouse;
import org.example.validators.ValidationHelper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
// I only wrote unit tests for the validators as they are very small and simple methods(units) so they do not have anything to
// test with integration tests.
public class ValidatorsUnitTests {

    @Test
    public void throwIfPrintingHouseIsNull_Should_ThrowException_When_Null() {
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> ValidationHelper.throwIfPrintingHouseIsNull(null));
    }

    @Test
    public void throwIfPrintingHouseIsNull_Should_NotThrowException_When_NotNull() {
        PrintingHouse printingHouse = new PrintingHouse();
        assertDoesNotThrow(() -> ValidationHelper.throwIfPrintingHouseIsNull(printingHouse));
    }

    @Test
    public void throwIfNegative_Should_ThrowException_When_NegativeValue() {
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> ValidationHelper.throwIfNegative(new BigDecimal("-1.00")));
    }

    //    Combined the two tests into one as the logic is very simple and doesnt require two tests
    @Test
    public void throwIfNegative_Should_NotThrowException_When_NonNegativeValueAndZero() {
        assertDoesNotThrow(() -> ValidationHelper.throwIfNegative(BigDecimal.ZERO));
        assertDoesNotThrow(() -> ValidationHelper.throwIfNegative(new BigDecimal("1.00")));
    }
}
