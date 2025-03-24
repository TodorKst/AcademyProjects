package integration;

import org.example.enums.DeviceType;
import org.example.exceptions.InvalidInputException;
import org.example.models.VacuumCleaner;
import org.example.services.VacuumCleanerServiceImpl;
import org.example.services.contracts.VacuumCleanerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class VacuumCleanerServiceIntegrationTests {
    private final VacuumCleanerService service = VacuumCleanerServiceImpl.getInstance();

    @Test
    public void clean_Should_ThrowException_When_TargetAreaIsNegative() {
        VacuumCleaner vacuum = new VacuumCleaner(
                "Test Vacuum",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.MOVABLE,
                false
        );

        Assertions.assertThrows(InvalidInputException.class, () -> service.clean(vacuum, -5));
    }

    @Test
    public void clean_Should_CapTargetAreaToMaxCleaningArea() {
        VacuumCleaner vacuum = new VacuumCleaner(
                "Test Vacuum",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.MOVABLE,
                false
        );

        int cleanedArea = service.clean(vacuum, 150);

        Assertions.assertEquals(10, cleanedArea);
    }

    @Test
    public void clean_Should_CleanAsMuchAsBatteryAllows() {
        VacuumCleaner vacuum = new VacuumCleaner(
                "Test Vacuum",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.MOVABLE,
                false
        );
        vacuum.setBatteryLevel(40);
        int cleanedArea = service.clean(vacuum, 6);

//        checks both the cleaned area and the battery level to make sure the battery level is reduced correctly
        Assertions.assertEquals(4, cleanedArea);
        Assertions.assertEquals(0, vacuum.getBatteryLevel());
    }

    @Test
    public void recharge_Should_SetBatteryLevelTo100_When_BatteryBelow100() {
        VacuumCleaner vacuum = new VacuumCleaner(
                "Test Vacuum",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.MOVABLE,
                false
        );
        vacuum.setBatteryLevel(50);
        service.recharge(vacuum);

        Assertions.assertEquals(100, vacuum.getBatteryLevel());
    }

    @Test
    public void recharge_Should_ThrowException_When_BatteryIsFull() {
        VacuumCleaner vacuum = new VacuumCleaner(
                "Test Vacuum",
                LocalDate.of(2022, 1, 1),
                50,
                DeviceType.MOVABLE,
                false
        );
        vacuum.setBatteryLevel(100);

//        throws exception when trying to recharge a vacuum with full battery to avoid overcharging
        Assertions.assertThrows(InvalidInputException.class, () -> service.recharge(vacuum));
    }

    @Test
    public void getInstance_Should_ReturnSameInstance_WhenCalledMultipleTimes() {
        VacuumCleanerService instance1 = VacuumCleanerServiceImpl.getInstance();
        VacuumCleanerService instance2 = VacuumCleanerServiceImpl.getInstance();

        Assertions.assertSame(instance1, instance2);
    }
}
