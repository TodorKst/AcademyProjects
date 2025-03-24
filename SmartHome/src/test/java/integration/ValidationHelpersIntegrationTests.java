package integration;

import org.example.ValidationHelpers;
import org.example.exceptions.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidationHelpersIntegrationTests {
    @Test
    public void validateString_Should_ThrowException_When_Null() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateString(null, "String cannot be null or empty"));
    }

    @Test
    public void validateString_Should_ThrowException_When_Empty() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateString("", "String cannot be null or empty"));
    }

    @Test
    public void validateString_Should_NotThrowException_When_StringIsValid() {
        Assertions.assertDoesNotThrow(() -> ValidationHelpers.validateString("Valid", "Valid string"));
    }

    @Test
    public void validateInt_Should_ThrowException_When_NumberIsNegative() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateInt(-1, "Negative numbers are not allowed"));
    }

    @Test
    public void validateInt_Should_NotThrowException_When_NumberIsZeroOrPositive() {
        Assertions.assertDoesNotThrow(() -> {
            ValidationHelpers.validateInt(0, "Zero");
            ValidationHelpers.validateInt(5, "Positive number");
        });
    }

    @Test
    public void validateBigDecimal_Should_ThrowException_When_Null() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateBigDecimal(null, "cannot be null"));
    }

    @Test
    public void validateBigDecimal_Should_ThrowException_When_NumberIsNegative() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateBigDecimal(BigDecimal.valueOf(-10), "negative value not allowed"));
    }

    @Test
    public void validateBigDecimal_Should_NotThrowException_When_NumberIsZeroOrPositive() {
        Assertions.assertDoesNotThrow(() -> {
            ValidationHelpers.validateBigDecimal(BigDecimal.ZERO, "valid");
            ValidationHelpers.validateBigDecimal(BigDecimal.TEN, "valid");
        });
    }

    @Test
    public void validateDate_Should_ThrowException_When_DateIsNull() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateDate(null, "cannot be null"));
    }

    @Test
    public void validateDate_Should_ThrowException_When_DateIsInFuture() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateDate(LocalDate.now().plusDays(1), "date cannot be in the future"));
    }

    @Test
    public void validateDate_Should_NotThrowException_When_DateIsValid() {
        Assertions.assertDoesNotThrow(() -> {
            ValidationHelpers.validateDate(LocalDate.now(), "valid");
            ValidationHelpers.validateDate(LocalDate.now().minusDays(1), "valid");
        });
    }

    @Test
    public void validateEnum_Should_ThrowException_When_EnumValueIsNull() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateEnum(null, "enum value cannot be null"));
    }

    @Test
    public void validateEnum_Should_NotThrowException_When_EnumValueIsNotNull() {
        Assertions.assertDoesNotThrow(() -> ValidationHelpers.validateEnum("NonNullValue", "enum value valid"));
    }

    @Test
    public void validateBatteryLevel_Should_ThrowException_When_BatteryLevelIsNegative() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateBatteryLevel(-1, "battery level cannot be negative"));
    }

    @Test
    public void validateBatteryLevel_Should_ThrowException_When_BatteryLevelIsAbove100() {
        Assertions.assertThrows(InvalidInputException.class, () -> ValidationHelpers.validateBatteryLevel(101, "battery level cannot be above 100"));
    }

    @Test
    public void validateBatteryLevel_Should_NotThrowException_When_BatteryLevelIsValid() {
        Assertions.assertDoesNotThrow(() -> {
            ValidationHelpers.validateBatteryLevel(0, "battery level can be 0");
            ValidationHelpers.validateBatteryLevel(100, "battery level can be 100");
            ValidationHelpers.validateBatteryLevel(50, "battery level can be between 0 and 100");
        });
    }
}
