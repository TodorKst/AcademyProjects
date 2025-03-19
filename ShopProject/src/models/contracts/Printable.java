package models.contracts;

import java.io.IOException;
//allows for future proofing the code, as we can have printing of other types of receipts like invoices, reports, etc. (фактури)
//in this case, we have a single implementation that saves the receipt to a file as the app is just a prototype
public interface Printable {
    void printReceipt() throws IOException;
}
