package org.example.services.contracts;

import org.example.models.VacuumCleaner;

public interface VacuumCleanerService {
    int clean(VacuumCleaner vacuum, int targetArea);

    void recharge(VacuumCleaner vacuum);
}
