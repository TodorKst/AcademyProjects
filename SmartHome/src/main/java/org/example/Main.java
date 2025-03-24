package org.example;

import org.example.enums.DeviceType;
import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;
import org.example.models.SmartLight;
import org.example.models.VacuumCleaner;
import org.example.services.VacuumCleanerServiceImpl;
import org.example.services.contracts.SmartLightService;
import org.example.services.SmartLightServiceImpl;
import org.example.services.contracts.VacuumCleanerService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SmartLightService smartLightService = SmartLightServiceImpl.getInstance();
        VacuumCleanerService vacuumCleanerService = VacuumCleanerServiceImpl.getInstance();

        try {
            // Create a SmartLight with some example values
            SmartLight smartLight = new SmartLight(
                    "Smart Light",
                    LocalDate.of(2023, 5, 10),
                    10,
                    DeviceType.BUILT_IN,
                    LightingMode.DIM,
                    BigDecimal.valueOf(800)
            );

            System.out.println("Smart Light Initial Brightness: " + smartLightService.getCurrentLumenOutput(smartLight));

            smartLightService.increaseBrightness(smartLight);
            System.out.println("Brightness after increasing: " + smartLightService.getCurrentLumenOutput(smartLight));

            // Try increasing beyond max brightness (should throw an exception)
            smartLightService.increaseBrightness(smartLight);
            smartLightService.increaseBrightness(smartLight); // This should trigger the exception, and it should be caught and output in the console
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
            int cleaned = vacuumCleanerService.clean(vacuum, 3);
            System.out.println("Cleaned area: " + cleaned + "m²");
            System.out.println("Battery after cleaning: " + vacuum.getBatteryLevel());

            // Try to clean area that will go beyond battery capacity
            vacuumCleanerService.clean(vacuum, 100); // May trigger an error depending on remaining battery

            System.out.println("Battery: " + vacuum.getBatteryLevel());
            vacuumCleanerService.recharge(vacuum);
            System.out.println("Battery: " + vacuum.getBatteryLevel());
            vacuumCleanerService.recharge(vacuum);
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}