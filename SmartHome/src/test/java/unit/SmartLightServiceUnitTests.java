package unit;

import org.example.enums.LightingMode;
import org.example.exceptions.InvalidInputException;
import org.example.models.SmartLight;
import org.example.services.SmartLightServiceImpl;
import org.example.services.contracts.SmartLightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class SmartLightServiceUnitTests {
    private SmartLightService service;
    private SmartLight lightMock;

    @BeforeEach
    public void setup() {
        service = SmartLightServiceImpl.getInstance();
        lightMock = Mockito.mock(SmartLight.class);
    }

    @Test
    public void increaseBrightness_Should_SetModeToMedium_When_CurrentModeIsDim() {
        when(lightMock.getLightingMode()).thenReturn(LightingMode.DIM);
        service.increaseBrightness(lightMock);
        verify(lightMock).setLightingMode(LightingMode.MEDIUM);
    }

    @Test
    public void increaseBrightness_Should_ThrowException_When_CurrentModeIsStrong() {
        when(lightMock.getLightingMode()).thenReturn(LightingMode.STRONG);
        Assertions.assertThrows(InvalidInputException.class, () -> service.increaseBrightness(lightMock));

//        test works without this line, but it makes sure the setter is never called.
        verify(lightMock, never()).setLightingMode(any());
    }

    @Test
    public void decreaseBrightness_Should_SetModeToMedium_When_CurrentModeIsStrong() {
        when(lightMock.getLightingMode()).thenReturn(LightingMode.STRONG);
        service.decreaseBrightness(lightMock);
        verify(lightMock).setLightingMode(LightingMode.MEDIUM);
    }

    @Test
    public void decreaseBrightness_Should_ThrowException_When_CurrentModeIsDim() {
        when(lightMock.getLightingMode()).thenReturn(LightingMode.DIM);
        Assertions.assertThrows(InvalidInputException.class, () -> service.decreaseBrightness(lightMock));

//        test works without this line, but it makes sure the setter is never called.
        verify(lightMock, never()).setLightingMode(any());
    }

    @Test
    public void getCurrentLumenOutput_Should_Return_CorrectLumenOutput() {
        BigDecimal maxOutput = BigDecimal.valueOf(800);
        when(lightMock.getMaxLumenOutput()).thenReturn(maxOutput);
        when(lightMock.getLightingMode()).thenReturn(LightingMode.MEDIUM);

        BigDecimal expectedOutput = maxOutput.multiply(LightingMode.MEDIUM.getLumenOutputFactor());

        Assertions.assertEquals(expectedOutput, service.getCurrentLumenOutput(lightMock));
    }

    @Test
    public void getInstance_Should_ReturnSameInstance_WhenCalledMultipleTimes() {
        SmartLightService instance1 = SmartLightServiceImpl.getInstance();
        SmartLightService instance2 = SmartLightServiceImpl.getInstance();
        Assertions.assertSame(instance1, instance2);
    }
}
