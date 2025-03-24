package org.example.models;

import org.example.ValidationHelpers;
import org.example.enums.DeviceType;
import org.example.enums.LightingMode;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SmartLight extends Device {
    private static final String LIGHTING_MODE_INPUT_ERROR_MESSAGE = "Lighting mode cannot be null.";
    private static final String MAX_LUMEN_OUTPUT_INPUT_ERROR_MESSAGE = "Max lumen output cannot be a negative value.";

    private LightingMode lightingMode;
    private BigDecimal maxLumenOutput;

    public SmartLight(String model,
                      LocalDate productionDate,
                      int powerConsumption,
                      DeviceType type,
                      LightingMode lightingMode,
                      BigDecimal maxLumenOutput) {
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

    public BigDecimal getMaxLumenOutput() {
        return maxLumenOutput;
    }

    public void setMaxLumenOutput(BigDecimal maxLumenOutput) {
        ValidationHelpers.validateBigDecimal(maxLumenOutput, MAX_LUMEN_OUTPUT_INPUT_ERROR_MESSAGE);
        this.maxLumenOutput = maxLumenOutput;
    }


}
