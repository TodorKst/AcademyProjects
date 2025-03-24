package org.example;

import java.time.LocalDate;

public class ValidationHelpers {
    public static void validateString(String string, String message) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateInt(int number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateDate(LocalDate date, String message) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateEnum(Object enumValue, String message) {
        if (enumValue == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateFloat(float number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
