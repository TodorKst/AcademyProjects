package org.example.services;

import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;
import org.example.models.SmartLight;
import org.example.services.contracts.SmartLightService;

import java.math.BigDecimal;

public class SmartLightServiceImpl implements SmartLightService {
    private static final String MIN_BRIGHTNESS_ERROR_MESSAGE = "Can't decrease brightness. Already at minimum.";
    private static final String MAX_BRIGHTNESS_ERROR_MESSAGE = "Can't increase brightness. Already at maximum.";

    private static SmartLightService instance;

    private SmartLightServiceImpl() {
    }

    public static SmartLightService getInstance() {
        synchronized (SmartLightServiceImpl.class) {
            if (instance == null) {
                return new SmartLightServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public void increaseBrightness(SmartLight light) {
        LightingMode[] modes = LightingMode.values();
        int nextIndex = light.getLightingMode().ordinal() + 1;
        if (nextIndex < modes.length) {
            light.setLightingMode(modes[nextIndex]);
        } else throw new InvalidInputException(MAX_BRIGHTNESS_ERROR_MESSAGE);
    }

    @Override
    public void decreaseBrightness(SmartLight light) {
        LightingMode[] modes = LightingMode.values();
        int prevIndex = light.getLightingMode().ordinal() - 1;
        if (prevIndex >= 0) {
            light.setLightingMode(modes[prevIndex]);
        } else throw new InvalidInputException(MIN_BRIGHTNESS_ERROR_MESSAGE);
    }

    @Override
    public BigDecimal getCurrentLumenOutput(SmartLight light) {
        return light.getMaxLumenOutput().multiply(light.getLightingMode().getLumenOutputFactor());
    }
}
