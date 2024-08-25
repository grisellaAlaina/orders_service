package org.example.ordersservice.controller;

import org.example.ordersservice.dto.OrderDto;
import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/byID")
    public OrderResponseDto findByID(@RequestParam("id") int id) {
        return orderService.getById(id);
    }

    @GetMapping("/byDateAndSum")
    public List<OrderResponseDto> findByDateAndSum(@RequestParam("date") LocalDate date,
                                                   @RequestParam("total") int sum) {
        return orderService.getByDateAndSum(date, sum);
    }

    @GetMapping("/exceptAndPeriod")
    public List<OrderResponseDto> findByExceptAndPeriod(@RequestParam("except")String except,
                                                        @RequestParam("minDate") LocalDate minDate,
                                                        @RequestParam("maxDate") LocalDate maxDate) {
        return orderService.getByExceptAndPeriod(except, minDate, maxDate);
    }
}
