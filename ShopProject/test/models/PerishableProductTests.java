package models;

import enums.ProductCategory;
import exceptions.ProductExpiredException;
import models.product.PerishableProduct;
import models.product.ProductImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PerishableProductTests {
    private PerishableProduct product;
    private Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop();

        product = new PerishableProduct("P001", "Milk", ProductCategory.FOOD, BigDecimal.valueOf(10), LocalDate.now().plusDays(3));
        shop = new Shop();
        shop.setPerishableMarkup(BigDecimal.valueOf(1.5));
        shop.setDiscountThreshold(5);
        shop.setDiscountPercentage(BigDecimal.valueOf(0.10));
    }

    @Test
    void getDiscountThreshold_Should_ReturnThreshold() {
        shop.setDiscountThreshold(5);
        assertEquals(5, shop.getDiscountThreshold());
    }

    @Test
    void setDiscountThreshold_Should_UpdateThreshold() {
        shop.setDiscountThreshold(7);
        assertEquals(7, shop.getDiscountThreshold());
    }

    @Test
    void getDiscountPercentage_Should_ReturnPercentage() {
        shop.setDiscountPercentage(BigDecimal.valueOf(0.10));
        assertEquals(BigDecimal.valueOf(0.10), shop.getDiscountPercentage());
    }

    @Test
    void setDiscountPercentage_Should_UpdatePercentage() {
        shop.setDiscountPercentage(BigDecimal.valueOf(0.15));
        assertEquals(BigDecimal.valueOf(0.15), shop.getDiscountPercentage());
    }

    @Test
    void getPerishableMarkup_Should_ReturnMarkup() {
        shop.setPerishableMarkup(BigDecimal.valueOf(1.5));
        assertEquals(BigDecimal.valueOf(1.5), shop.getPerishableMarkup());
    }

    @Test
    void setPerishableMarkup_Should_UpdateMarkup() {
        shop.setPerishableMarkup(BigDecimal.valueOf(1.7));
        assertEquals(BigDecimal.valueOf(1.7), shop.getPerishableMarkup());
    }

    @Test
    void getNonPerishableMarkup_Should_ReturnMarkup() {
        shop.setNonPerishableMarkup(BigDecimal.valueOf(1.2));
        assertEquals(BigDecimal.valueOf(1.2), shop.getNonPerishableMarkup());
    }

    @Test
    void setNonPerishableMarkup_Should_UpdateMarkup() {
        shop.setNonPerishableMarkup(BigDecimal.valueOf(1.3));
        assertEquals(BigDecimal.valueOf(1.3), shop.getNonPerishableMarkup());
    }

    @Test
    void getExpiryDate_Should_ReturnCorrectDate() {
        assertEquals(LocalDate.now().plusDays(3), product.getExpiryDate());
    }

    @Test
    void calculateSellingPrice_Should_ThrowException_WhenProductExpired() {
        ProductImpl expiredProduct = new PerishableProduct("P002", "Yogurt", ProductCategory.FOOD, BigDecimal.valueOf(5), LocalDate.now().minusDays(1));
        assertThrows(ProductExpiredException.class, () -> expiredProduct.calculateSellingPrice(shop, LocalDate.now()));
    }

    @Test
    void calculateSellingPrice_Should_ApplyDiscount_WhenWithinThreshold() throws ProductExpiredException {

        PerishableProduct discountedProduct = new PerishableProduct("P003", "Cheese", ProductCategory.FOOD, BigDecimal.valueOf(20), LocalDate.now().plusDays(3));

        BigDecimal expectedPrice = BigDecimal.valueOf(20)
                .multiply(shop.getPerishableMarkup())
                .multiply(BigDecimal.ONE.subtract(shop.getDiscountPercentage()));

        BigDecimal actualPrice = discountedProduct.calculateSellingPrice(shop, LocalDate.now());

        assertEquals(0, expectedPrice.compareTo(actualPrice), "The discounted selling price is incorrect.");
    }

}
