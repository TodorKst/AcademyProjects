package org.example;

import org.example.models.CashDesk;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CashDeskTests {

    @Test
    void testGetDeskNumber_Should_Work() {
        CashDesk cashDesk = new CashDesk(1);
        Assertions.assertEquals(1, cashDesk.getDeskNumber());
    }

    @Test
    void testGetCustomerQueue_Should_Work() {
        CashDesk cashDesk = new CashDesk(1);
        Assertions.assertNotNull(cashDesk.getCustomerQueue());
    }

    @Test
    void testGetCustomerQueue_Should_Return_New_Queue() {
        CashDesk cashDesk = new CashDesk(1);
        Assertions.assertNotSame(cashDesk.getCustomerQueue(), cashDesk.getCustomerQueue());
    }

    @Test
    void testGetCustomerQueue_Should_Return_Empty_Queue() {
        CashDesk cashDesk = new CashDesk(1);
        Assertions.assertTrue(cashDesk.getCustomerQueue().isEmpty());
    }
}
