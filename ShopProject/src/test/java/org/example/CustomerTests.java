package org.example;

import org.example.enums.ProductCategory;
import org.example.exceptions.InvalidInputException;
import org.example.models.contracts.Product;
import org.example.models.person.Customer;
import org.example.models.product.ProductImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTests {

    private Customer customer;
    private ProductImpl product;

    @BeforeEach
    void setUp() {
        customer = new Customer("C001", "John Doe", BigDecimal.valueOf(100));
        product = new ProductImpl("P001", "Apple", ProductCategory.FOOD, BigDecimal.valueOf(2));
    }

    @Test
    void getId_Should_ReturnCorrectId() {
        assertEquals("C001", customer.getId());
    }

    @Test
    void getName_Should_ReturnCorrectName() {
        assertEquals("John Doe", customer.getName());
    }

    @Test
    void setName_Should_UpdateCustomerName() {
        customer.setName("Jane Doe");
        assertEquals("Jane Doe", customer.getName());
    }

    @Test
    void getBalance_Should_ReturnCorrectBalanceWithTwoDecimalPlaces() {
        assertEquals(BigDecimal.valueOf(100.00).setScale(2, BigDecimal.ROUND_HALF_EVEN), customer.getBalance());
    }

    @Test
    void setBalance_Should_UpdateBalanceCorrectly() {
        customer.setBalance(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50.00).setScale(2, BigDecimal.ROUND_HALF_EVEN), customer.getBalance());
    }

    @Test
    void getShoppingCart_Should_ReturnEmptyCartInitially() {
        assertTrue(customer.getShoppingCart().isEmpty());
    }

    @Test
    void addToShoppingCart_Should_AddProductToCart() {
        customer.addToShoppingCart(product, 3);
        Map<Product, Integer> cart = customer.getShoppingCart();

        assertTrue(cart.containsKey(product));
        assertEquals(3, cart.get(product));
    }

    @Test
    void addToShoppingCart_Should_IncreaseQuantityWhenProductAlreadyInCart() {
        customer.addToShoppingCart(product, 2);
        customer.addToShoppingCart(product, 3);

        assertEquals(5, customer.getShoppingCart().get(product));
    }

    @Test
    void deductBalance_Should_DecreaseBalanceCorrectly() {
        customer.deductBalance(BigDecimal.valueOf(30));
        assertEquals(BigDecimal.valueOf(70.00).setScale(2, BigDecimal.ROUND_HALF_EVEN), customer.getBalance());
    }

    @Test
    void deductBalance_Should_ThrowException_WhenAmountIsNegative() {
        assertThrows(InvalidInputException.class, () -> customer.deductBalance(BigDecimal.valueOf(-10)));
    }

    @Test
    void deductBalance_Should_ThrowException_WhenInsufficientFunds() {
        assertThrows(InvalidInputException.class, () -> customer.deductBalance(BigDecimal.valueOf(150)));
    }
}
