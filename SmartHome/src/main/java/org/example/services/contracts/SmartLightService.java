package org.example.services.contracts;

import org.example.models.SmartLight;

import java.math.BigDecimal;

public interface SmartLightService {
    void increaseBrightness(SmartLight light);

    void decreaseBrightness(SmartLight light);

    BigDecimal getCurrentLumenOutput(SmartLight light);
}
