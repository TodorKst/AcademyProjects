package models;

import enums.ProductCategory;
import models.product.NonPerishableProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonPerishableProductTests {
    private NonPerishableProduct product;
    private Shop shop;

    @BeforeEach
    void setUp() {
        product = new NonPerishableProduct("123", "Canned Beans", ProductCategory.FOOD, BigDecimal.valueOf(10.0));
        shop = new Shop();
        shop.setNonPerishableMarkup(BigDecimal.valueOf(1.2));
    }

    @Test
    void getBasePrice_Should_ReturnBasePrice() {
        assertEquals(BigDecimal.valueOf(10.0), product.getBasePrice());
    }

    @Test
    void setBasePrice_Should_UpdateBasePrice() {
        product.setBasePrice(BigDecimal.valueOf(15.0));
        assertEquals(BigDecimal.valueOf(15.0), product.getBasePrice());
    }

    @Test
    void calculateSellingPrice_Should_ApplyMarkupCorrectly() {
        BigDecimal expectedPrice = BigDecimal.valueOf(10.0).multiply(BigDecimal.valueOf(1.2));
        assertEquals(expectedPrice, product.calculateSellingPrice(shop, LocalDate.now()));
    }
}
