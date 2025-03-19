package models.receipt;

import models.contracts.Printable;
import models.person.Cashier;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Receipt implements Serializable, Printable {

    //    all fields are final, as the receipt serves as a record and should not be modified
    private final String receiptId;
    private final Cashier cashier;
    private final LocalDate purchaseDate;
    private final List<ReceiptItem> items;
    private final BigDecimal totalAmount;

    public Receipt(Cashier cashier, LocalDate purchaseDate, List<ReceiptItem> items, BigDecimal totalAmount) {
//        the substring method is there to shorten the uuid so the file name and content is more readable
        this.receiptId = UUID.randomUUID().toString().substring(0, 8);
        this.cashier = cashier;
        this.purchaseDate = purchaseDate;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void printReceipt() throws IOException {
        File dir = new File("human-readable-receipts");
        if (!dir.exists()) {
            dir.mkdirs(); // Create directory if it doesn't exist
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(dir + File.separator + getReceiptId() + ".txt"))) {
            writer.println("Receipt ID: " + getReceiptId());
            writer.println("Cashier: " + getCashier().getName() + " (" + getCashier().getId() + ")");
            writer.println("Purchase Date: " + getPurchaseDate());
            writer.println("Items:");
            for (ReceiptItem item : getItems()) {
                writer.println("  Product: " + item.getProduct().getName());
                writer.println("    Quantity: " + item.getQuantity());
                writer.println("    Unit Price: " + item.getUnitPrice());
                writer.println("    Total Price: " + item.getTotalPrice());
            }
            writer.println("Total Amount: " + getTotalAmount());
        }
    }

    public void serializeReceipt(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs(); // Create directory if it doesn't exist
        }
        File file = new File(dir, receiptId + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Receipt> deserializeReceipts(String directoryPath) {
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

}
