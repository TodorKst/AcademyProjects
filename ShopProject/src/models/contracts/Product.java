package models.contracts;

import enums.ProductCategory;
import exceptions.ProductExpiredException;
import models.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Product {
    String getId();
    String getName();
    ProductCategory getCategory();
    BigDecimal getBasePrice();
    BigDecimal calculateSellingPrice(Shop shop, LocalDate currentDate) throws ProductExpiredException;
}
