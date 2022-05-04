package com.programmers.gccoffee.product.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Product {

    private final UUID productId;
    private String productName;
    private Category category;
    private long price;
    private String description;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID productId, String productName,
        Category category, long price, String description, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        if (price <= 0) {
            throw new IllegalArgumentException("price는 0보다 커야합니다.");
        }
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(UUID productId, String productName,
        Category category, long price, String description) {
        if (price <= 0) {
            throw new IllegalArgumentException("price는 0보다 커야합니다.");
        }
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Product updateProduct(String productName,
        Category category, long price, String description) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.updatedAt = LocalDateTime.now().withNano(0);

        return this;
    }
}
