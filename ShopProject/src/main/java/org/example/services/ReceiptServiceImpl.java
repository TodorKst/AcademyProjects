package org.example.services;

import org.example.models.receipt.Receipt;
import org.example.models.receipt.ReceiptItem;
import org.example.services.contracts.ReceiptService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {

    @Override
    public List<Receipt> deserializeReceipts(String directoryPath) {
        List<Receipt> receipts = new ArrayList<>();
        File dir = new File(directoryPath);

        if (!dir.exists() || !dir.isDirectory()) {
            return receipts; // Return empty list if directory doesn't exist
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Receipt receipt = (Receipt) ois.readObject();
                    receipts.add(receipt);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return receipts;
    }

    @Override
    public void printReceipt(Receipt receipt) throws IOException {
        File dir = new File("human-readable-receipts");
        if (!dir.exists()) {
            dir.mkdirs(); // Create directory if it doesn't exist
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
            dir.mkdirs(); // Create directory if it doesn't exist
        }
        File file = new File(dir, receipt.getReceiptId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
