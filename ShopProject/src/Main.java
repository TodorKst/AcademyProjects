import models.contracts.Product;
import enums.ProductCategory;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import exceptions.ProductExpiredException;
import models.CashDesk;
import models.Shop;
import models.person.Cashier;
import models.person.Customer;
import models.product.NonPerishableProduct;
import models.product.PerishableProduct;
import models.receipt.Receipt;
import services.ShopServiceImpl;
import services.contracts.ShopService;

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

        System.out.println("Loading previous receipts...");
        List<Receipt> previousReceipts = Receipt.deserializeReceipts("receipts");
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

        ShopService shopService = new ShopServiceImpl();

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
//            receipt.serializeReceipt("receipts"); // this method saves the receipt to a file
//            receipt.printReceipt(); //this method prints the receipt to a file that a human can read

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

        System.out.println("Total Costs: " + shopService.calculateTotalCosts(shop));
        System.out.println("Total Income: " + shopService.calculateTotalIncome(shop));
//      profit is negative because the shop has made only one sale and the costs are higher than the income mainly from wages of cashiers
        System.out.println("Profit: " + shopService.calculateProfit(shop));
    }
}