package org.example;

import org.example.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidationHelpers {
    public static void validateString(String string, String message) {
        if (string == null || string.isEmpty()) {
            throw new InvalidInputException(message);
        }
    }

    public static void validateInt(int number, String message) {
        if (number < 0) {
            throw new InvalidInputException(message);
        }
    }

    public static void validateBigDecimal(BigDecimal number, String message) {
        if (number == null || number.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(message);
        }
    }

    public static void validateDate(LocalDate date, String message) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new InvalidInputException(message);
        }
    }

    public static void validateEnum(Object enumValue, String message) {
        if (enumValue == null) {
            throw new InvalidInputException(message);
        }
    }

    public static void validateBatteryLevel(int batteryLevel, String message) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new InvalidInputException(message);
        }
    }
}
