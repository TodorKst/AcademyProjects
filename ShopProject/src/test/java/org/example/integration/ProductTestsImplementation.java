package org.example.integration;

import org.example.enums.ProductCategory;
import org.example.models.product.ProductImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTestsImplementation {
    private ProductImpl product;

    @BeforeEach
    void setUp() {
        product = new ProductImpl("001", "Laptop", ProductCategory.NON_FOOD, BigDecimal.valueOf(1000.0));
    }

    @Test
    void getId_Should_ReturnId() {
        assertEquals("001", product.getId());
    }

    @Test
    void getName_Should_ReturnName() {
        assertEquals("Laptop", product.getName());
    }

    @Test
    void setName_Should_UpdateName() {
        product.setName("Gaming Laptop");
        assertEquals("Gaming Laptop", product.getName());
    }

    @Test
    void getCategory_Should_ReturnCategory() {
        assertEquals(ProductCategory.NON_FOOD, product.getCategory());
    }

    @Test
    void setCategory_Should_UpdateCategory() {
        product.setCategory(ProductCategory.FOOD);
        assertEquals(ProductCategory.FOOD, product.getCategory());
    }

    @Test
    void getBasePrice_Should_ReturnBasePrice() {
        assertEquals(BigDecimal.valueOf(1000.0), product.getBasePrice());
    }

    @Test
    void setBasePrice_Should_UpdateBasePrice() {
        product.setBasePrice(BigDecimal.valueOf(1200.0));
        assertEquals(BigDecimal.valueOf(1200.0), product.getBasePrice());
    }
}
