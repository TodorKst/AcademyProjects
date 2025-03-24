package unit;

import org.example.exceptions.InvalidInputException;
import org.example.models.VacuumCleaner;
import org.example.services.VacuumCleanerServiceImpl;
import org.example.services.contracts.VacuumCleanerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class VacuumCleanerServiceUnitTests {
    private VacuumCleanerService service;
    private VacuumCleaner vacuumMock;

    @BeforeEach
    public void setup() {
        service = VacuumCleanerServiceImpl.getInstance();
        vacuumMock = Mockito.mock(VacuumCleaner.class);
    }

    @Test
    public void clean_Should_ThrowException_When_TargetAreaIsNegative() {
        Assertions.assertThrows(InvalidInputException.class, () -> service.clean(vacuumMock, -10));

//        test works without this line, but it makes sure the setter is never called.
        verify(vacuumMock, never()).getBatteryLevel();
    }

    @Test
    public void clean_Should_SetBatteryLevelCorrectly_When_BatteryIsSufficient() {
        when(vacuumMock.hasMop()).thenReturn(false);
        when(vacuumMock.getBatteryLevel()).thenReturn(100);

        int cleanedArea = service.clean(vacuumMock, 5);

        Assertions.assertEquals(5, cleanedArea);
        verify(vacuumMock).setBatteryLevel(50);
    }

    @Test
    public void clean_Should_CapCleaningArea_When_TargetExceedsMaxCleaningArea() {
        when(vacuumMock.hasMop()).thenReturn(false);
        when(vacuumMock.getBatteryLevel()).thenReturn(100);

        int cleanedArea = service.clean(vacuumMock, 150);

        Assertions.assertEquals(10, cleanedArea);
        verify(vacuumMock).setBatteryLevel(0);
    }

    @Test
    public void recharge_Should_SetBatteryLevelTo100_When_BatteryBelow100() {
        when(vacuumMock.getBatteryLevel()).thenReturn(50);
        service.recharge(vacuumMock);

        verify(vacuumMock).setBatteryLevel(100);
    }

    @Test
    public void recharge_Should_ThrowException_When_BatteryIsFull() {
        when(vacuumMock.getBatteryLevel()).thenReturn(100);
        Assertions.assertThrows(InvalidInputException.class, () -> service.recharge(vacuumMock));

//        test works without this line, but it makes sure the setter is never called
        verify(vacuumMock, never()).setBatteryLevel(anyInt());
    }

    @Test
    public void getInstance_Should_ReturnSameInstance_WhenCalledMultipleTimes() {
        VacuumCleanerService instance1 = VacuumCleanerServiceImpl.getInstance();
        VacuumCleanerService instance2 = VacuumCleanerServiceImpl.getInstance();
        Assertions.assertSame(instance1, instance2);
    }
}
