package org.example.models;

import org.example.ValidationHelpers;
import org.example.enums.DeviceType;
import org.example.exceptions.InvalidInputException;

import java.time.LocalDate;

public abstract class Device {
    private static final String MODEL_INPUT_ERROR_MESSAGE = "Model name cannot be null or empty.";
    private static final String PRODUCTION_DATE_INPUT_ERROR_MESSAGE = "Production date cannot be in the future.";
    private static final String DEVICE_TYPE_ERROR_MESSAGE = "Device type cannot be null.";
    private static final String POWER_CONSUMPTION_INPUT_ERROR_MESSAGE = "Power consumption cannot be a negative value.";

    private final String model;
    private final LocalDate productionDate;
    private final DeviceType type;
    private int powerConsumption;

    public Device(String model,
                  LocalDate productionDate,
                  int powerConsumption,
                  DeviceType type) {
        validateInputs(model, productionDate, type);
        this.model = model;
        this.productionDate = productionDate;
        this.type = type;

        setPowerConsumption(powerConsumption);
    }

    private void validateInputs(String model, LocalDate productionDate, DeviceType type) {
        ValidationHelpers.validateString(model, MODEL_INPUT_ERROR_MESSAGE);
        ValidationHelpers.validateDate(productionDate, PRODUCTION_DATE_INPUT_ERROR_MESSAGE);
        ValidationHelpers.validateEnum(type, DEVICE_TYPE_ERROR_MESSAGE);
    }


    public String getModel() {
        return model;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public int getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(int powerConsumption) {

        ValidationHelpers.validateInt(powerConsumption, POWER_CONSUMPTION_INPUT_ERROR_MESSAGE);
        this.powerConsumption = powerConsumption;
    }

    public DeviceType getType() {
        return type;
    }


}
