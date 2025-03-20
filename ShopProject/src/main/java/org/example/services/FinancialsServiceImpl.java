package org.example.services;

import org.example.exceptions.InvalidInputException;
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
        throwIfShopIsNull(shop);

        throwIfCashiersOrInventoryAreInvalid(shop);

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
        throwIfShopIsNull(shop);

        throwIfReceiptsAreInvalid(shop);

        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Receipt receipt : shop.getReceipts()) {
            totalIncome = totalIncome.add(receipt.getTotalAmount());
        }

        return totalIncome;
    }


    @Override
    public BigDecimal calculateProfit(Shop shop) {
        throwIfShopIsNull(shop);

        return calculateTotalIncome(shop).subtract(calculateTotalCosts(shop));
    }

    private void throwIfCashiersOrInventoryAreInvalid(Shop shop) {
        if (shop.getCashiers().isEmpty() || shop.getInventory().isEmpty()) {
            throw new InvalidInputException("Shop must have cashiers or stock items to calculate total costs.");
        }
    }

    private void throwIfShopIsNull(Shop shop) {
        if (shop == null) {
            throw new InvalidInputException("Shop must be provided.");
        }
    }

    private void throwIfReceiptsAreInvalid(Shop shop) {
        if (shop.getReceipts().isEmpty()) {
            throw new InvalidInputException("Shop must have receipts to calculate total income.");
        }
    }

}
