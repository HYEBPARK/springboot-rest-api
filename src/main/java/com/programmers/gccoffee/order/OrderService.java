package com.programmers.gccoffee.order;

import com.programmers.gccoffee.order.model.Order;
import com.programmers.gccoffee.order.model.OrderItem;
import com.programmers.gccoffee.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(String email, String address, String postcode, List<OrderItem> orderItems) {
        var order = new Order(UUID.randomUUID(), email, address, postcode, orderItems);
        return orderRepository.insert(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public boolean deleteById(UUID orderId) {

        return orderRepository.deleteById(orderId);
    }
}
