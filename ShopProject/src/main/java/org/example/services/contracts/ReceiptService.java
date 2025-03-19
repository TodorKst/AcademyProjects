package org.example.services.contracts;

import org.example.models.receipt.Receipt;

import java.io.IOException;
import java.util.List;

public interface ReceiptService {
    List<Receipt> deserializeReceipts(String directoryPath);

    void printReceipt(Receipt receipt) throws IOException;

    void serializeReceipt(Receipt receipt, String directoryPath);
}
