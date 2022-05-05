package com.programmers.gccoffee.product.model;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("price가 0이하면 IllegalException 발생해야함")
    void price() {
        assertThatThrownBy(
            () -> new Product(UUID.randomUUID(), "커피1", Category.COFFEE_BEAN_PACKAGE, 0L,
                "커피입니다")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(
            () -> new Product(UUID.randomUUID(), "커피2", Category.COFFEE_BEAN_PACKAGE, -2L,
                "커피입니다")).isInstanceOf(IllegalArgumentException.class);

    }
}