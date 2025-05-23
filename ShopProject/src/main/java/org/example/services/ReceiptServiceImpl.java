package org.example.services;

import org.example.exceptions.UnsuccessfulDeserializationException;
import org.example.exceptions.UnsuccessfulSerializationException;
import org.example.models.receipt.Receipt;
import org.example.models.receipt.ReceiptItem;
import org.example.services.contracts.ReceiptService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {

    private static ReceiptServiceImpl instance;

    private ReceiptServiceImpl() {
    }

    public static ReceiptServiceImpl getInstance() {
        synchronized (ReceiptServiceImpl.class) {
            if (instance == null) {
                instance = new ReceiptServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public List<Receipt> deserializeReceipts(String directoryPath) {
        List<Receipt> receipts = new ArrayList<>();
        File dir = new File(directoryPath);

        if (!dir.exists() || !dir.isDirectory()) {
            return receipts;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Receipt receipt = (Receipt) ois.readObject();
                    receipts.add(receipt);
                } catch (IOException | ClassNotFoundException e) {
                    throw new UnsuccessfulDeserializationException("Failed to deserialize receipt from file: " + file.getName());
                }
            }
        }

        return receipts;
    }

    @Override
    public void printReceipt(Receipt receipt) throws IOException {
        File dir = new File("human-readable-receipts");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(dir + File.separator + receipt.getReceiptId() + ".txt"))) {
            writer.println("Receipt ID: " + receipt.getReceiptId());
            writer.println("Cashier: " + receipt.getCashier().getName() + " (" + receipt.getCashier().getId() + ")");
            writer.println("Purchase Date: " + receipt.getPurchaseDate());
            writer.println("Items:");
            for (ReceiptItem item : receipt.getItems()) {
                writer.println("  Product: " + item.getProduct().getName());
                writer.println("    Quantity: " + item.getQuantity());
                writer.println("    Unit Price: " + item.getUnitPrice());
                writer.println("    Total Price: " + item.getTotalPrice());
            }
            writer.println("Total Amount: " + receipt.getTotalAmount());
        }
    }

    @Override
    public void serializeReceipt(Receipt receipt, String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, receipt.getReceiptId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(receipt);
        } catch (IOException e) {
            throw new UnsuccessfulSerializationException("Failed to serialize receipt to file: " + file.getName());
        }
    }

}
