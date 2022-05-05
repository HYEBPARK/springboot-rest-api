package com.programmers.gccoffee.order.controller;

import com.programmers.gccoffee.order.model.OrderItem;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@NotNull
public class OrderDto {

    @Email
    private String email;
    private String address;
    @Length(min = 5, max = 5)
    private String postcode;
    private List<OrderItem> orderItems;

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
