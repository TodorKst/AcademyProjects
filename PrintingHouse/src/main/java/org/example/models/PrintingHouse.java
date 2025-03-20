package org.example.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintingHouse {
    private static BigDecimal PROFIT_TAX_PERCENTAGE = new BigDecimal("0.10");

    private List<Order> orders;

    public PrintingHouse() {
        orders = new ArrayList<>();
    }

    public static BigDecimal getProfitTaxPercentage() {
        return PROFIT_TAX_PERCENTAGE;
    }

    public static void setProfitTaxPercentage(BigDecimal profitTaxPercentage) {
        PROFIT_TAX_PERCENTAGE = profitTaxPercentage;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}