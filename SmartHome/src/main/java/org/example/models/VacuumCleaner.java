package org.example.models;

import org.example.enums.DeviceType;
import org.example.exceptions.InvalidInputException;

import java.time.LocalDate;

public class VacuumCleaner extends Device {
    private static final int MAX_CLEANING_AREA = 100;
    private static final int BATTERY_CONSUMPTION_PER_M2 = 10;
    private static final int BATTERY_CONSUMPTION_WITH_MOP = 20;
    private static final String INVALID_TARGET_AREA_ERROR_MESSAGE = "Target area cannot be negative.";
    public static final String BATTERY_LEVEL_INPUT_ERROR_MESSAGE = "Battery level must be between 0 and 100.";

    private boolean hasMop;
    private int batteryLevel;

    public VacuumCleaner(String model, LocalDate productionDate, int powerConsumption, DeviceType type, boolean hasMop) {
        super(model, productionDate, powerConsumption, type);
        this.hasMop = hasMop;
        this.batteryLevel = 100;
    }

    public int clean(int targetArea) {
        if (targetArea < 0) {
            throw new InvalidInputException(INVALID_TARGET_AREA_ERROR_MESSAGE);
        }


        targetArea = Math.min(targetArea, MAX_CLEANING_AREA);

        int consumptionRate = hasMop ? BATTERY_CONSUMPTION_WITH_MOP : BATTERY_CONSUMPTION_PER_M2;
        int maxCleanableArea = batteryLevel / consumptionRate;

        int cleanedArea = Math.min(targetArea, maxCleanableArea);
        batteryLevel = Math.max(0, batteryLevel - (cleanedArea * consumptionRate));

        if (maxCleanableArea < targetArea) {
            System.out.println("Not enough battery to complete cleaning process. Will clean as much as possible.");
        }
        return cleanedArea;
    }

    public void recharge() {
        if (this.batteryLevel >= 100) {
            throw new InvalidInputException("Battery is full. Stopped charging to prevent damage.");
        }
        batteryLevel = 100;
    }

    public boolean hasMop() {
        return hasMop;
    }

    public void setHasMop(boolean hasMop) {
        this.hasMop = hasMop;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new InvalidInputException(BATTERY_LEVEL_INPUT_ERROR_MESSAGE);
        }
        this.batteryLevel = batteryLevel;
    }
}
