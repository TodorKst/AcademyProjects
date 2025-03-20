package org.example.services;


import org.example.models.Order;
import org.example.models.PrintingHouse;
import org.example.services.contracts.PrintingHouseService;

import java.math.BigDecimal;

public class PrintingHouseServiceImpl implements PrintingHouseService {
    private static PrintingHouseService instance;
    private BigDecimal markupPercentage;

    private PrintingHouseServiceImpl() {
        markupPercentage = new BigDecimal("0.15");
    }

    public static PrintingHouseService getInstance() {
        if (instance == null) {
            instance = new PrintingHouseServiceImpl();
        }
        return instance;
    }

    public BigDecimal getMarkupPercentage() {
        return markupPercentage;
    }

    public  void setMarkupPercentage(BigDecimal markupPercentage) {
        this.markupPercentage = markupPercentage;
    }

    @Override
    public BigDecimal calculateTotalExpenses(PrintingHouse printingHouse) {
        return printingHouse.getOrders().stream()
                .map(Order::calculateTotalPaperCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalIncome(PrintingHouse printingHouse) {
        return printingHouse.getOrders().stream()
                .map(Order::calculateTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.ONE.add(markupPercentage));
    }

    @Override
    public BigDecimal calculateProfit(PrintingHouse printingHouse) {
        return calculateTotalIncome(printingHouse).subtract(calculateTotalExpenses(printingHouse));
    }

    @Override
    public BigDecimal calculateNetProfit(PrintingHouse printingHouse) {
        BigDecimal profit = calculateProfit(printingHouse);
        BigDecimal tax = profit.multiply(PrintingHouse.getProfitTaxPercentage());
        return profit.subtract(tax);
    }

}
