package org.example.models;

import org.example.models.person.Customer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class CashDesk implements Serializable {
    private int deskNumber;
    private final Queue<Customer> customerQueue;

    public CashDesk(int deskNumber) {
        this.deskNumber = deskNumber;
        this.customerQueue = new LinkedList<Customer>();
    }

    public int getDeskNumber() {
        return deskNumber;
    }

    public Queue<Customer> getCustomerQueue() {
        return new LinkedList<>(customerQueue);
    }
}
