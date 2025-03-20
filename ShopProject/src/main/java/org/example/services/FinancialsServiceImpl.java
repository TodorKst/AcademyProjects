package org.example.services;

import org.example.models.Shop;
import org.example.models.StockItem;
import org.example.models.person.Cashier;
import org.example.models.receipt.Receipt;
import org.example.services.contracts.FinancialsService;

import java.math.BigDecimal;

public class FinancialsServiceImpl implements FinancialsService {

    private static FinancialsServiceImpl instance;

    private FinancialsServiceImpl() {
    }

    public static FinancialsServiceImpl getInstance() {
        if (instance == null) {
            instance = new FinancialsServiceImpl();
        }
        return instance;
    }

    @Override
    public BigDecimal calculateTotalCosts(Shop shop) {
        BigDecimal totalCosts = BigDecimal.ZERO;

        for (Cashier cashier : shop.getCashiers()) {
            totalCosts = totalCosts.add(cashier.getSalary());
        }

        for (StockItem stockItem : shop.getInventory().values()) {
            BigDecimal deliveryCost = stockItem.getDeliveryCost();
            totalCosts = totalCosts.add(deliveryCost);
        }

        return totalCosts;
    }

    @Override
    public BigDecimal calculateTotalIncome(Shop shop) {
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Receipt receipt : shop.getReceipts()) {
            totalIncome = totalIncome.add(receipt.getTotalAmount());
        }

        return totalIncome;
    }

    @Override
    public BigDecimal calculateProfit(Shop shop) {
        return calculateTotalIncome(shop).subtract(calculateTotalCosts(shop));
    }


}
