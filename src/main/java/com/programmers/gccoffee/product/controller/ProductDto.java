package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.model.Category;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ProductDto {

    @NotNull(message = "productName은 필수 입력입니다.")
    @Length(max = 15, message = "productName은 15자 이내로 작성해주세요.")
    private String productName;

    @NotNull(message = "category는 필수 입력입니다.")
    private Category category;

    @NotNull(message = "price는 필수 입력입니다.")
    @Min(value = 0, message = "price는 0보다 커야합니다.")
    private long price;

    @Length(max = 500, message = "description은 500자 이내로 작성해주세요.")
    private String description;

    public ProductDto(String productName,
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