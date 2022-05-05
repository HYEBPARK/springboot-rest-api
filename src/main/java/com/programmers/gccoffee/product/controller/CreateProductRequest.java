package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.model.Category;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateProductRequest {

    @NotNull
    @Length(min = 2, max = 10)
    private String productName;
    @NotNull
    private Category category;
    @NotNull
    @Min(0)
    private long price;
    @Length(max = 500)
    private String description;

    public CreateProductRequest(String productName,
        Category category, long price, String description) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
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
}