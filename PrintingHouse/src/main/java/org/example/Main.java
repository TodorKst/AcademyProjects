package org.example;


import org.example.enums.PaperSize;
import org.example.models.Order;
import org.example.models.PrintingHouse;
import org.example.models.publication.Book;
import org.example.models.publication.Newspaper;
import org.example.services.PrintingHouseServiceImpl;
import org.example.services.contracts.PrintingHouseService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        PrintingHouse printingHouse = new PrintingHouse();
        PrintingHouseService service = PrintingHouseServiceImpl.getInstance();

        Book book = new Book("Java Programming",
                LocalDate.of(2023, 5, 10),
                300,
                PaperSize.A4,
                new BigDecimal("2.50"));

        Newspaper newspaper = new Newspaper("Daily News",
                LocalDate.of(2023, 5, 12),
                40,
                PaperSize.A3);

        Order bookOrder = new Order(book, 100);
        Order newspaperOrder = new Order(newspaper, 500);

        printingHouse.getOrders().add(bookOrder);
        printingHouse.getOrders().add(newspaperOrder);

        System.out.println("Total Paper Cost: " + service.calculateTotalExpenses(printingHouse));
        System.out.println("Total Income: " + service.calculateTotalIncome(printingHouse));
        System.out.println("Total Profit: " + service.calculateProfit(printingHouse));
        System.out.println("Net Profit: " + service.calculateNetProfit(printingHouse));
    }
}
