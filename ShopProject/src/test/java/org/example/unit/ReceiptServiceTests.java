package org.example.unit;

import org.example.models.contracts.Product;
import org.example.models.person.Cashier;
import org.example.models.receipt.Receipt;
import org.example.models.receipt.ReceiptItem;
import org.example.services.ReceiptServiceImpl;
import org.example.services.contracts.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReceiptServiceTests {

    private ReceiptService receiptService;
    private Receipt mockReceipt;
    private Cashier mockCashier;
    private ReceiptItem mockItem;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        receiptService = ReceiptServiceImpl.getInstance();

        mockReceipt = mock(Receipt.class);
        mockCashier = mock(Cashier.class);
        mockItem = mock(ReceiptItem.class);
        mockProduct = mock(Product.class);

        when(mockReceipt.getReceiptId()).thenReturn("receipt123");
        when(mockReceipt.getCashier()).thenReturn(mockCashier);
        when(mockReceipt.getPurchaseDate()).thenReturn(LocalDate.of(2025, 3, 20));
        when(mockReceipt.getItems()).thenReturn(List.of(mockItem));
        when(mockReceipt.getTotalAmount()).thenReturn(new BigDecimal("99.99"));

        when(mockCashier.getName()).thenReturn("John Doe");
        when(mockCashier.getId()).thenReturn("id1");

        when(mockItem.getProduct()).thenReturn(mockProduct);
        when(mockItem.getQuantity()).thenReturn(2);
        when(mockItem.getUnitPrice()).thenReturn(new BigDecimal("10.50"));
        when(mockItem.getTotalPrice()).thenReturn(new BigDecimal("21.00"));

        when(mockProduct.getName()).thenReturn("Sample Product");
    }


    @Test
    void deserializeReceipts_Should_ReturnEmptyList_When_DirectoryDoesNotExist() {
        List<Receipt> receipts = receiptService.deserializeReceipts("non_existent_dir");
        assertTrue(receipts.isEmpty());
    }

    @Test
    void deserializeReceipts_Should_ReturnEmptyList_When_DirectoryIsEmpty(@TempDir Path tempDir) {
        List<Receipt> receipts = receiptService.deserializeReceipts(tempDir.toString());
        assertTrue(receipts.isEmpty());
    }

    @Test
    void serializeReceipt_Should_CreateFile(@TempDir Path tempDir) throws IOException {
        String directory = tempDir.toString();
        String receiptId = mockReceipt.getReceiptId();

        receiptService.serializeReceipt(mockReceipt, directory);

        Path receiptFile = tempDir.resolve(receiptId + ".ser");
        assertTrue(Files.exists(receiptFile), "Receipt file should be created.");
    }

    @Test
    void serializeAndDeserializeReceipt_Should_ReturnEquivalentReceipt(@TempDir Path tempDir) {
        String directory = tempDir.toString();
        Cashier cashier = new Cashier("id1", "John Doe", null, null);
        Receipt receiptToSerialize = new Receipt(cashier, LocalDate.of(2025, 3, 20), List.of(mockItem), new BigDecimal("99.99"));
        when(mockReceipt.getReceiptId()).thenReturn(receiptToSerialize.getReceiptId());

        receiptService.serializeReceipt(receiptToSerialize, directory);
        List<Receipt> deserializedReceipts = receiptService.deserializeReceipts(directory);

        assertTrue(deserializedReceipts.size() == 1, "There should be exactly one deserialized receipt.");
        Receipt deserializedReceipt = deserializedReceipts.getFirst();

        assertEquals(mockReceipt.getReceiptId(), deserializedReceipt.getReceiptId(), "Receipt ID should match.");
        assertEquals(mockReceipt.getCashier().getName(), deserializedReceipt.getCashier().getName(), "Cashier name should match.");
        assertEquals(mockReceipt.getPurchaseDate(), deserializedReceipt.getPurchaseDate(), "Purchase date should match.");
        assertEquals(mockReceipt.getTotalAmount(), deserializedReceipt.getTotalAmount(), "Total amount should match.");
    }

    @Test
    void serializeReceipt_Should_CreateDirectoryIfNotExists(@TempDir Path tempDir) {
        String nonExistentDir = tempDir.resolve("new_directory").toString();
        Cashier cashier = new Cashier("id1", "John Doe", null, null);
        Receipt receipt = new Receipt(cashier, LocalDate.of(2025, 3, 20), List.of(), null);

        receiptService.serializeReceipt(receipt, nonExistentDir);

        File directory = new File(nonExistentDir);
        assertTrue(directory.exists() && directory.isDirectory(), "Directory should be created.");

        File receiptFile = new File(directory, receipt.getReceiptId() + ".ser");
        assertTrue(receiptFile.exists(), "Receipt file should be created in the new directory.");
    }

    @Test
    void printReceipt_Should_GenerateCorrectReceiptContent() throws IOException {
        File dir = new File("human-readable-receipts");
        File receiptFile = new File(dir, mockReceipt.getReceiptId() + ".txt");

        if (receiptFile.exists()) {
            receiptFile.delete();
        }

        receiptService.printReceipt(mockReceipt);

        assertTrue(receiptFile.exists(), "Receipt file should be created in 'human-readable-receipts'.");

        String actualContent = Files.readString(receiptFile.toPath());

        String expectedContent = "Receipt ID: receipt123\n" +
                "Cashier: John Doe (id1)\n" +
                "Purchase Date: 2025-03-20\n" +
                "Items:\n" +
                "  Product: Sample Product\n" +
                "    Quantity: 2\n" +
                "    Unit Price: 10.50\n" +
                "    Total Price: 21.00\n" +
                "Total Amount: 99.99\n";

//        the part inside the equals is there to remove whitespaces from the beginning and end of the string they dont affect the content of the file
        assertEquals(
                expectedContent.replace("\r\n", "\n").trim(),
                actualContent.replace("\r\n", "\n").trim(),
                "Receipt content should match expected output."
        );
    }




}