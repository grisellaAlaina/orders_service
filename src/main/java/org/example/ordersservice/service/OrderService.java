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

    private String generateOrderNumber() {
        String orderNumberApiUrl = "http://localhost:8081/numbers";
        return restTemplate.getForObject(orderNumberApiUrl, String.class);
    }
}
