package org.example.ordersservice.service;

import org.example.ordersservice.dto.OrderDto;
import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.model.OrderDetail;
import org.example.ordersservice.model.UserOrder;
import org.example.ordersservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    private ModelMapper modelMapper;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        orderService = new OrderService(orderRepository, modelMapper, restTemplate);
    }

    @Test
    void createOrderTest() {
        OrderDto orderDto = new OrderDto();

        UserOrder expectedOrder = new UserOrder();
        OrderDetail expectedOrderDetail = new OrderDetail();

        when(orderRepository.createOrder(any())).thenReturn(expectedOrder);
        when(orderRepository.createOrderDetail(any())).thenReturn(expectedOrderDetail);

        OrderResponseDto actualResponse = orderService.createOrder(orderDto);

        assertEquals(expectedOrder.getOrderNumber(), actualResponse.getOrderNumber());
        assertEquals(expectedOrderDetail.getProductName(), actualResponse.getProductName());
    }

    @Test
    void getByIdTest() {
        int id = 1;
        OrderResponseDto mockResponse = new OrderResponseDto();
        // Set mockResponse fields...
        when(orderRepository.getById(id)).thenReturn(mockResponse);
        OrderResponseDto actualResponse = orderService.getById(id);
        assertEquals(mockResponse, actualResponse);
    }

    @Test
    void getByDateAndSumTest() {
        LocalDate date = LocalDate.now();
        int sum = 1000;
        List<OrderResponseDto> mockResponse = List.of(new OrderResponseDto(), new OrderResponseDto());
        when(orderRepository.getByDateAndTotalSum(date, sum)).thenReturn(mockResponse);
        List<OrderResponseDto> actualResponse = orderService.getByDateAndSum(date, sum);
        assertEquals(mockResponse, actualResponse);
    }

    @Test
    void getByExceptAndPeriodTest() {
        String except = "test";
        LocalDate minDate = LocalDate.now().minusDays(1);
        LocalDate maxDate = LocalDate.now();
        List<OrderResponseDto> mockResponse = List.of(new OrderResponseDto(), new OrderResponseDto());
        when(orderRepository.getByExceptAndPeriod(except, minDate, maxDate)).thenReturn(mockResponse);
        List<OrderResponseDto> actualResponse = orderService.getByExceptAndPeriod(except, minDate, maxDate);
        assertEquals(mockResponse, actualResponse);
    }

    @Test
    void generateOrderNumberTest() {
        String mockResponse = "testOrderNumber";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        String actualResponse = orderService.generateOrderNumber();
        assertEquals(mockResponse, actualResponse);
    }
}
