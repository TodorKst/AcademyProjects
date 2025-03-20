package org.example.validators;

import org.example.exceptions.InvalidInputException;
import org.example.models.PrintingHouse;

import java.math.BigDecimal;

public class ValidationHelper {
    public static void throwIfPrintingHouseIsNull(PrintingHouse printingHouse) {
        if (printingHouse == null) {
            throw new InvalidInputException("Printing house cannot be null.");
        }
    }

    public static void throwIfNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Value cannot be negative.");
        }
    }
}
