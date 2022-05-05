package com.programmers.gccoffee.order.model;

import com.programmers.gccoffee.product.model.Category;
import java.util.UUID;

public record OrderItem(UUID orderId, UUID productId, Category category, long price, int quantity){

}
