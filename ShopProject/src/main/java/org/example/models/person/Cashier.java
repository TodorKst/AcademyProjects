package org.example.models.person;

import org.example.models.CashDesk;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cashier extends PersonImpl implements Serializable {

    private CashDesk cashDesk;
    private BigDecimal salary;

    public Cashier(String id, String name, CashDesk cashDesk, BigDecimal salary) {
        super(id, name);
        this.cashDesk = cashDesk;
        this.salary = salary;
    }

    public Cashier(String id, String name) {
    }

    public CashDesk getCashDesk() {
        return cashDesk;
    }

    public void setCashDesk(CashDesk cashDesk) {
        this.cashDesk = cashDesk;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
