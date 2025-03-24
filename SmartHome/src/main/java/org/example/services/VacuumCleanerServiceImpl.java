package org.example.services;

import org.example.exceptions.InvalidInputException;
import org.example.models.VacuumCleaner;
import org.example.services.contracts.VacuumCleanerService;

public class VacuumCleanerServiceImpl implements VacuumCleanerService {
    private static final String INVALID_TARGET_AREA_ERROR_MESSAGE = "Target area cannot be negative.";

    private static final int MAX_CLEANING_AREA = 100;
    private static final int BATTERY_CONSUMPTION_PER_M2 = 10;
    private static final int BATTERY_CONSUMPTION_WITH_MOP = 20;

    private static VacuumCleanerService instance;

    private VacuumCleanerServiceImpl() {
    }

    public static VacuumCleanerService getInstance() {
        synchronized (SmartLightServiceImpl.class) {
            if (instance == null) {
                return new VacuumCleanerServiceImpl();
            }
        }
        return instance;
    }




    @Override
    public int clean(VacuumCleaner vacuum, int targetArea) {
        if (targetArea < 0) {
            throw new InvalidInputException(INVALID_TARGET_AREA_ERROR_MESSAGE);
        }


        targetArea = Math.min(targetArea, MAX_CLEANING_AREA);

        int consumptionRate = vacuum.hasMop() ? BATTERY_CONSUMPTION_WITH_MOP : BATTERY_CONSUMPTION_PER_M2;
        int maxCleanableArea = vacuum.getBatteryLevel() / consumptionRate;

        int cleanedArea = Math.min(targetArea, maxCleanableArea);
        vacuum.setBatteryLevel(Math.max(0, vacuum.getBatteryLevel()) - (cleanedArea * consumptionRate));

        if (maxCleanableArea < targetArea) {
            System.out.println("Not enough battery to complete cleaning process. Will clean as much as possible.");
        }
        return cleanedArea;
    }

    @Override
    public void recharge(VacuumCleaner vacuum) {
        if (vacuum.getBatteryLevel() >= 100) {
            throw new InvalidInputException("Battery is full. Stopped charging to prevent damage.");
        }
        vacuum.setBatteryLevel(100);
    }
}
