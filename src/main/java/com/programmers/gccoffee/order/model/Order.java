package com.programmers.gccoffee.order.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final String email;
    private  String address;
    private  String postcode;
    private List<OrderItem> orderItems;
    private  OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, String email, String address, String postcode,
        List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.ACCEPTED;
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public Order(UUID orderId, String email, String address, String postcode,
        List<OrderItem> orderItems, OrderStatus orderStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(UUID orderId, String email, String address, String postcode,
        OrderStatus orderStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
