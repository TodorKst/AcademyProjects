package org.example.services.contracts;

import org.example.models.PrintingHouse;

import java.math.BigDecimal;

public interface PrintingHouseService {
    BigDecimal calculateTotalExpenses(PrintingHouse printingHouse);

    BigDecimal calculateTotalIncome(PrintingHouse printingHouse);

    BigDecimal calculateProfit(PrintingHouse printingHouse);

    BigDecimal calculateNetProfit(PrintingHouse printingHouse);
}
