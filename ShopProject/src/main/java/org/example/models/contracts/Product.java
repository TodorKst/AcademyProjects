package org.example.models.contracts;

import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Product {
    String getId();
    String getName();
    org.example.enums.ProductCategory getCategory();
    BigDecimal getBasePrice();
    BigDecimal calculateSellingPrice(Shop shop, LocalDate currentDate) throws ProductExpiredException;
}
