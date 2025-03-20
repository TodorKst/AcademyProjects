package org.example.models.product;

import org.example.enums.ProductCategory;
import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PerishableProduct extends ProductImpl {
    //    expiryDate is final because it cannot change after the product is manufactured
    private final LocalDate expiryDate;

    public PerishableProduct(String id, String name, ProductCategory category, BigDecimal basePrice, LocalDate expiryDate) {
        super(id, name, category, basePrice);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public BigDecimal calculateSellingPrice(Shop shop, LocalDate currentDate) throws ProductExpiredException {
        if (currentDate.isAfter(expiryDate)) {
            throw new ProductExpiredException("Product " + getName() + " is expired.");
        }


        BigDecimal sellingPrice = getBasePrice().multiply(shop.getPerishableMarkup());


        long daysToExpiry = ChronoUnit.DAYS.between(currentDate, expiryDate);

        if (daysToExpiry <= shop.getDiscountThreshold()) {
            BigDecimal discount = shop.getDiscountPercentage();
            sellingPrice = sellingPrice.multiply(BigDecimal.ONE.subtract(discount));
        }
        return sellingPrice;
    }
}
