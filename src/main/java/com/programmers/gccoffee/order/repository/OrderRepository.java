package com.programmers.gccoffee.order.repository;

import com.programmers.gccoffee.order.model.Order;
import com.programmers.gccoffee.order.model.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderRepository {

    Order insert(Order order);

    List<Order> findAll();

    boolean deleteById(UUID orderId);

}
