package org.example;

import org.example.enums.ProductCategory;
import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InsufficientStockException;
import org.example.exceptions.ProductExpiredException;
import org.example.exceptions.UnsuccessfulDeserializationException;
import org.example.models.CashDesk;
import org.example.models.Shop;
import org.example.models.contracts.Product;
import org.example.models.person.Cashier;
import org.example.models.person.Customer;
import org.example.models.product.NonPerishableProduct;
import org.example.models.product.PerishableProduct;
import org.example.models.receipt.Receipt;
import org.example.services.FinancialsServiceImpl;
import org.example.services.ReceiptServiceImpl;
import org.example.services.ShopServiceImpl;
import org.example.services.contracts.FinancialsService;
import org.example.services.contracts.ReceiptService;
import org.example.services.contracts.ShopService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Shop shop = new Shop(
                "My Shop",
                new BigDecimal("1.20"),
                new BigDecimal("1.30"),
                5,
                new BigDecimal("0.10")
        );

        ShopService shopService = ShopServiceImpl.getInstance();
        ReceiptService receiptService = ReceiptServiceImpl.getInstance();
        FinancialsService financialsService = FinancialsServiceImpl.getInstance();

        System.out.println("Loading previous receipts...");
        List<Receipt> previousReceipts = null;
        try {
            previousReceipts = receiptService.deserializeReceipts("receipts");
        } catch (UnsuccessfulDeserializationException e) {
            System.out.println("Failed to load previous receipts: " + e.getMessage());
        }
        System.out.println("Total previous receipts loaded: " + previousReceipts.size());
        System.out.println();
        System.out.println("====================================");
        for (Receipt receipt : previousReceipts) {
            System.out.println("Receipt ID: " + receipt.getReceiptId());
            System.out.println("Total Amount: " + receipt.getTotalAmount());
            System.out.println("Cashier: " + receipt.getCashier().getName());
            System.out.println("Purchase Date: " + receipt.getPurchaseDate());

            System.out.println("====================================");
        }

        System.out.println();


        LocalDate milkExpiry = LocalDate.now().plusDays(10);
        Product milk = new PerishableProduct("P001", "Milk", ProductCategory.FOOD, new BigDecimal("2.00"), milkExpiry);

        Product soap = new NonPerishableProduct("NP001", "Soap", ProductCategory.NON_FOOD, new BigDecimal("1.50"));

        shop.addStock(milk, 10, BigDecimal.valueOf(2));
        shop.addStock(soap, 20, BigDecimal.valueOf(1));

        CashDesk desk = new CashDesk(1);
        Cashier cashier = new Cashier("C001", "John Doe", desk, new BigDecimal("1000.00"));
        shop.addCashier(cashier);

        Customer customer = new Customer("CU001", "Alice", new BigDecimal("50.00"));

        customer.addToShoppingCart(milk, 2);
        customer.addToShoppingCart(soap, 3);

        try {
            Receipt receipt = shopService.processSale(shop, cashier, customer, LocalDate.now());
//            receiptService.serializeReceipt(receipt, "receipts"); // this method saves the receipt to a file
//            receiptService.printReceipt(receipt); //this method prints the receipt to a file that a human can read

//          to disable saving the receipt to a file just comment out the 2 lines above

//          in a normal use case only one of these methods would be used but i wanted to show both

//          I spent a good amount of time trying to serialize to json without using and external library so i can keep the scope of the project in control
//          but it was too much work so i had to settle for this somewhat worse option of having one method for serialization and one for printing which bugs me a bit
            System.out.println("Sale successful!");
            System.out.println("Receipt ID: " + receipt.getReceiptId());
            System.out.println("Total Amount: " + receipt.getTotalAmount());
            System.out.println("Remaining Customer Balance: " + customer.getBalance());
        } catch (InsufficientStockException | InsufficientFundsException | ProductExpiredException e) {
            System.out.println("Sale failed: " + e.getMessage());
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("Total Costs: " + financialsService.calculateTotalCosts(shop));
        System.out.println("Total Income: " + financialsService.calculateTotalIncome(shop));
//      profit is negative because the shop has made only one sale and the costs are higher than the income mainly from wages of cashiers
        System.out.println("Profit: " + financialsService.calculateProfit(shop));
    }
}