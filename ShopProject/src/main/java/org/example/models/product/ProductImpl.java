package org.example.models.product;


import org.example.enums.ProductCategory;
import org.example.exceptions.ProductExpiredException;
import org.example.models.Shop;
import org.example.models.contracts.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductImpl implements Serializable, Product {
    private final String id;
    private String name;
    private ProductCategory category;
    private BigDecimal basePrice;

    public ProductImpl(String id, String name, ProductCategory category, BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }


    public BigDecimal calculateSellingPrice(Shop shop, LocalDate currentDate) throws ProductExpiredException {
        return getBasePrice().multiply(shop.getNonPerishableMarkup());
    }


}
