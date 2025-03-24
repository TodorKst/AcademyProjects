package org.example.models;

import org.example.ValidationHelpers;
import org.example.enums.DeviceType;
import org.example.exceptions.InvalidInputException;

import java.time.LocalDate;

public class VacuumCleaner extends Device {
    public static final String BATTERY_LEVEL_INPUT_ERROR_MESSAGE = "Battery level must be between 0 and 100.";

    private boolean hasMop;
    private int batteryLevel;

    public VacuumCleaner(String model, LocalDate productionDate, int powerConsumption, DeviceType type, boolean hasMop) {
        super(model, productionDate, powerConsumption, type);
        this.hasMop = hasMop;
        this.batteryLevel = 100;
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
        ValidationHelpers.validateBatteryLevel(batteryLevel, BATTERY_LEVEL_INPUT_ERROR_MESSAGE);
        this.batteryLevel = batteryLevel;
    }


}
