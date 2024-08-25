package org.example.ordersservice.service;

import org.example.ordersservice.dto.OrderDto;
import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.model.UserOrder;
import org.example.ordersservice.model.OrderDetail;
import org.example.ordersservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    public OrderResponseDto createOrder(OrderDto orderDto) {

        UserOrder order = modelMapper.map(orderDto, UserOrder.class);
        order.setOrderDate(LocalDate.now());
        order.setOrderNumber(generateOrderNumber());
        OrderDetail orderDetail = modelMapper.map(orderDto, OrderDetail.class);

        order = orderRepository.createOrder(order);
        orderDetail.setOrderId(order.getId());
        orderDetail = orderRepository.createOrderDetail(orderDetail);

        OrderResponseDto responseDto = modelMapper.map(order, OrderResponseDto.class);
        modelMapper.map(orderDetail, responseDto);

        return responseDto;
    }

    public OrderResponseDto getById(int id) {
        return orderRepository.getById(id);
    }

    public List<OrderResponseDto> getByDateAndSum(LocalDate date, int sum) {
        return orderRepository.getByDateAndTotalSum(date, sum);
    }

    public List<OrderResponseDto> getByExceptAndPeriod(String except, LocalDate minDate, LocalDate maxDate) {
        return orderRepository.getByExceptAndPeriod(except, minDate, maxDate);
    }

    private String generateOrderNumber() {
        String orderNumberApiUrl = "http://localhost:8081/numbers";
        return restTemplate.getForObject(orderNumberApiUrl, String.class);
    }
}
