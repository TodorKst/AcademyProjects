package org.example;

import org.example.enums.DeviceType;
import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;
import org.example.models.SmartLight;
import org.example.models.VacuumCleaner;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a SmartLight with some example values
            SmartLight smartLight = new SmartLight(
                    "Smart Light",
                    LocalDate.of(2023, 5, 10),
                    10,
                    DeviceType.BUILT_IN,
                    LightingMode.DIM,
                    800
            );

            System.out.println("Smart Light Initial Brightness: " + smartLight.getCurrentLumenOutput());

            smartLight.increaseBrightness();
            System.out.println("Brightness after increasing: " + smartLight.getCurrentLumenOutput());

            // Try increasing beyond max brightness (should throw an exception)
            smartLight.increaseBrightness();
            smartLight.increaseBrightness(); // This should trigger the exception, and it should be caught and output in the console
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n----------------------\n");

        try {
            // Create our vacuum
            VacuumCleaner vacuum = new VacuumCleaner(
                    "Roomba",
                    LocalDate.of(2022, 8, 15),
                    40,
                    DeviceType.MOVABLE,
                    true
            );

            System.out.println("Vacuum Battery Level: " + vacuum.getBatteryLevel());

            // Attempt to clean an area of 3m²
            int cleaned = vacuum.clean(3);
            System.out.println("Cleaned area: " + cleaned + "m²");
            System.out.println("Battery after cleaning: " + vacuum.getBatteryLevel());

            // Try to clean area that will go beyond battery capacity
            vacuum.clean(100); // May trigger an error depending on remaining battery

            System.out.println("Battery: " + vacuum.getBatteryLevel());
            vacuum.recharge();
            System.out.println("Battery: " + vacuum.getBatteryLevel());
            vacuum.recharge();
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}