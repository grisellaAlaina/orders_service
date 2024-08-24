package org.example.ordersservice.controller;

import org.example.ordersservice.dto.OrderDto;
import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderResponseDto createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }
}
