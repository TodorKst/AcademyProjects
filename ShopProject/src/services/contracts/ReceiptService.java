package services.contracts;

import models.receipt.Receipt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public interface ReceiptService {
    List<Receipt> deserializeReceipts(String directoryPath);

    void printReceipt(Receipt receipt) throws IOException;

    void serializeReceipt(Receipt receipt, String directoryPath);
}
