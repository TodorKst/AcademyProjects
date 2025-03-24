package integration;

import org.example.enums.DeviceType;
import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;
import org.example.models.SmartLight;
import org.example.services.SmartLightServiceImpl;
import org.example.services.contracts.SmartLightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SmartLightServiceIntegrationTests {
    private final SmartLightService service = SmartLightServiceImpl.getInstance();

    @Test
    public void increaseBrightness_Should_ChangeModeToMedium_When_ModeIsDim() {
        SmartLight light = new SmartLight(
                "Test Light",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.BUILT_IN,
                LightingMode.DIM,
                BigDecimal.valueOf(1000)
        );
        service.increaseBrightness(light);

        Assertions.assertEquals(LightingMode.MEDIUM, light.getLightingMode());
    }

    @Test
    public void increaseBrightness_Should_ThrowException_When_ModeIsStrong() {
        SmartLight light = new SmartLight(
                "Test Light",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.BUILT_IN,
                LightingMode.STRONG,
                BigDecimal.valueOf(1000)
        );

        Assertions.assertThrows(InvalidInputException.class, () -> service.increaseBrightness(light));
    }

    @Test
    public void decreaseBrightness_Should_ChangeModeToMedium_When_ModeIsStrong() {
        SmartLight light = new SmartLight(
                "Test Light",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.BUILT_IN,
                LightingMode.STRONG,
                BigDecimal.valueOf(1000)
        );
        service.decreaseBrightness(light);

        Assertions.assertEquals(LightingMode.MEDIUM, light.getLightingMode());
    }

    @Test
    public void decreaseBrightness_Should_ThrowException_When_ModeIsDim() {
        SmartLight light = new SmartLight(
                "Test Light",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.BUILT_IN,
                LightingMode.DIM,
                BigDecimal.valueOf(1000)
        );

        Assertions.assertThrows(InvalidInputException.class, () -> service.decreaseBrightness(light));
    }

    @Test
    public void getCurrentLumenOutput_Should_Return_CorrectLumenOutput() {
        BigDecimal maxOutput = BigDecimal.valueOf(800);
        SmartLight light = new SmartLight(
                "Test Light",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.BUILT_IN,
                LightingMode.MEDIUM,
                maxOutput
        );
        BigDecimal expectedOutput = maxOutput.multiply(LightingMode.MEDIUM.getLumenOutputFactor());

        Assertions.assertEquals(expectedOutput, service.getCurrentLumenOutput(light));
    }

    @Test
    public void getInstance_Should_ReturnSameInstance_WhenCalledMultipleTimes() {
        SmartLightService instance1 = SmartLightServiceImpl.getInstance();
        SmartLightService instance2 = SmartLightServiceImpl.getInstance();

        Assertions.assertSame(instance1, instance2);
    }
}
