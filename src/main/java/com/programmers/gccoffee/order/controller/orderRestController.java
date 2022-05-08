package com.programmers.gccoffee.order.controller;

import com.programmers.gccoffee.order.OrderService;
import com.programmers.gccoffee.order.model.Order;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class orderRestController {

    private final static Logger log = LoggerFactory.getLogger(orderRestController.class);
    private final OrderService orderService;

    public orderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/order")
    public OrderDto postOrder(@RequestBody @Validated OrderDto orderDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(
                (error) -> log.error("orderDto value error -> {}", error.getDefaultMessage()));

            return null;
        }

        orderService.create(orderDto.getEmail(), orderDto.getAddress(),
            orderDto.getPostcode(),
            orderDto.getOrderItems());

        return orderDto;
    }

    @GetMapping("/api/v1/orders")
    public List<Order> getOrders() {

        return orderService.getOrders();
    }

    @DeleteMapping("/api/v1/orders/{orderId}")
    public UUID deleteOrderById(@PathVariable UUID orderId) {
        orderService.deleteById(orderId);

        return orderId;
    }
}
