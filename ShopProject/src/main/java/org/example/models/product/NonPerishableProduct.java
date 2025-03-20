package org.example.models.product;

import org.example.enums.ProductCategory;
import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NonPerishableProduct extends ProductImpl {

    public NonPerishableProduct(String id, String name, ProductCategory category, BigDecimal basePrice) {
        super(id, name, category, basePrice);
    }

    @Override
    public BigDecimal calculateSellingPrice(Shop shop, LocalDate currentDate) throws ProductExpiredException {
        return getBasePrice().multiply(shop.getNonPerishableMarkup());
    }
}
