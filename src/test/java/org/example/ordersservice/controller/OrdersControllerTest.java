package org.example.ordersservice.controller;

import org.example.ordersservice.dto.OrderDto;
import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Вы должны заменить пакет на свой.

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrderService orderService;

    private OrdersController ordersController;

    @BeforeEach
    void setUp() {
        ordersController = new OrdersController(orderService);
    }

    @Test
    void createOrderTest() {

        OrderDto orderDto = new OrderDto();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        when(orderService.createOrder(orderDto)).thenReturn(orderResponseDto);

        OrderResponseDto resultDto = ordersController.createOrder(orderDto);

        verify(orderService, times(1)).createOrder(orderDto);

        assertEquals(resultDto, orderResponseDto);
    }

    @Test
    void findByIDTest() {
        int id = 1;
        OrderResponseDto orderResponseDto = new OrderResponseDto();

        when(orderService.getById(id)).thenReturn(orderResponseDto);

        OrderResponseDto resultDto = ordersController.findByID(id);

        verify(orderService, times(1)).getById(id);
        assertEquals(resultDto, orderResponseDto);
    }

    @Test
    void findByDateAndSumTest() {
        LocalDate date = LocalDate.now();
        int sum = 100;
        List<OrderResponseDto> orders = new ArrayList<>();

        when(orderService.getByDateAndSum(date, sum)).thenReturn(orders);

        List<OrderResponseDto> resultOrders = ordersController.findByDateAndSum(date, sum);

        verify(orderService, times(1)).getByDateAndSum(date, sum);
        assertEquals(resultOrders, orders);
    }

    @Test
    void findByExceptAndPeriodTest() {
        String except = "Exception";
        LocalDate minDate = LocalDate.now();
        LocalDate maxDate = LocalDate.now();
        List<OrderResponseDto> orders = new ArrayList<>();

        when(orderService.getByExceptAndPeriod(except, minDate, maxDate)).thenReturn(orders);

        List<OrderResponseDto> resultOrders = ordersController.findByExceptAndPeriod(except, minDate, maxDate);

        verify(orderService, times(1)).getByExceptAndPeriod(except, minDate, maxDate);
        assertEquals(resultOrders, orders);
    }
}