package org.example.models;

import org.example.ValidationHelpers;
import org.example.contracts.BrightnessAdjustable;
import org.example.enums.DeviceType;
import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;

import java.time.LocalDate;

public class SmartLight extends Device implements BrightnessAdjustable {
    private static final String LIGHTING_MODE_INPUT_ERROR_MESSAGE = "Lighting mode cannot be null.";
    private static final String MAX_LUMEN_OUTPUT_INPUT_ERROR_MESSAGE = "Max lumen output cannot be a negative value.";
    private static final String MIN_BRIGHTNESS_ERROR_MESSAGE = "Can't decrease brightness. Already at minimum.";
    private static final String MAX_BRIGHTNESS_ERROR_MESSAGE = "Can't increase brightness. Already at maximum.";

    private LightingMode lightingMode;
    private int maxLumenOutput;

    public SmartLight(String model,
                      LocalDate productionDate,
                      int powerConsumption,
                      DeviceType type,
                      LightingMode lightingMode,
                      int maxLumenOutput) {
        super(model, productionDate, powerConsumption, type);
        setLightingMode(lightingMode);
        setMaxLumenOutput(maxLumenOutput);
    }

    public LightingMode getLightingMode() {
        return lightingMode;
    }

    public void setLightingMode(LightingMode lightingMode) {
        ValidationHelpers.validateEnum(lightingMode, LIGHTING_MODE_INPUT_ERROR_MESSAGE);
        this.lightingMode = lightingMode;
    }

    public int getMaxLumenOutput() {
        return maxLumenOutput;
    }

    public void setMaxLumenOutput(int maxLumenOutput) {
        ValidationHelpers.validateInt(maxLumenOutput, MAX_LUMEN_OUTPUT_INPUT_ERROR_MESSAGE);
        this.maxLumenOutput = maxLumenOutput;
    }

    public float getCurrentLumenOutput() {
        return maxLumenOutput * lightingMode.getLumenOutputFactor();
    }

    @Override
    public void increaseBrightness() {
        LightingMode[] modes = LightingMode.values();
        int nextIndex = lightingMode.ordinal() + 1;
        if (nextIndex < modes.length) {
            lightingMode = modes[nextIndex];
        } else throw new InvalidInputException(MAX_BRIGHTNESS_ERROR_MESSAGE);
    }

    @Override
    public void decreaseBrightness() {
        int prevIndex = lightingMode.ordinal() - 1;
        if (prevIndex >= 0) {
            lightingMode = LightingMode.values()[prevIndex];
        } else throw new InvalidInputException(MIN_BRIGHTNESS_ERROR_MESSAGE);
    }
}
