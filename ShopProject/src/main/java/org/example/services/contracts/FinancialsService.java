package org.example.services.contracts;

import org.example.models.Shop;

import java.math.BigDecimal;

public interface FinancialsService {
    BigDecimal calculateTotalCosts(Shop shop);

    BigDecimal calculateTotalIncome(Shop shop);

    BigDecimal calculateProfit(Shop shop);
}
