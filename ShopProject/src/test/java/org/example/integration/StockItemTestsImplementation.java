package org.example.integration;

import org.example.enums.ProductCategory;
import org.example.exceptions.InvalidInputException;
import org.example.models.StockItem;
import org.example.models.product.ProductImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockItemTestsImplementation {
    private StockItem stockItem;
    private ProductImpl mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new ProductImpl("id1", "Soap", ProductCategory.NON_FOOD, BigDecimal.TWO); // Assuming a constructor exists
        stockItem = new StockItem(mockProduct, 10, new BigDecimal("5.0"));
    }

    @Test
    void getProduct_Should_Return_Correct_Product() {
        assertEquals(mockProduct, stockItem.getProduct());
    }

    @Test
    void getQuantity_Should_Return_Correct_Value() {
        assertEquals(10, stockItem.getQuantity());
    }

    @Test
    void setQuantity_Should_Update_Quantity() {
        stockItem.setQuantity(20);
        assertEquals(20, stockItem.getQuantity());
    }

    @Test
    void getDeliveryCost_Should_Return_Correct_Value() {
        assertEquals(new BigDecimal("5.0"), stockItem.getDeliveryCost());
    }

    @Test
    void setDeliveryCost_Should_Update_Delivery_Cost() {
        stockItem.setDeliveryCost(new BigDecimal("7.5"));
        assertEquals(new BigDecimal("7.5"), stockItem.getDeliveryCost());
    }

    @Test
    void increaseQuantity_Should_Increment_Quantity_By_One() {
        stockItem.increaseQuantity();
        assertEquals(11, stockItem.getQuantity());
    }

    @Test
    void increaseQuantity_Should_Increment_Quantity_By_Given_Amount() {
        stockItem.increaseQuantity(5);
        assertEquals(15, stockItem.getQuantity());
    }

    @Test
    void decreaseQuantity_Should_Decrement_Quantity_By_One() {
        stockItem.decreaseQuantity();
        assertEquals(9, stockItem.getQuantity());
    }

    @Test
    void decreaseQuantity_Should_Decrement_Quantity_By_Given_Amount() {
        stockItem.decreaseQuantity(5);
        assertEquals(5, stockItem.getQuantity());
    }

    @Test
    void decreaseQuantity_Should_Throw_Exception_When_Insufficient_Stock() {
        assertThrows(InvalidInputException.class, () -> stockItem.decreaseQuantity(15));
    }
}
