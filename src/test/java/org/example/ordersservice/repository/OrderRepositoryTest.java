package org.example.ordersservice.repository;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.example.ordersservice.model.UserOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(jdbcTemplate);
    }

    @Test
    void createOrderCallsDatabase() {
        UserOrder inputOrder = mock(UserOrder.class);

        orderRepository.createOrder(inputOrder);

        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void getByIdCallsDatabase() {
        int id = 1;

        orderRepository.getById(id);

        verify(jdbcTemplate).queryForObject(any(String.class),
                any(RowMapper.class),
                any(Integer.class));
    }

    @Test
    void getByDateAndTotalSumCallsDatabase() {
        LocalDate date = LocalDate.now();
        int sum = 123;

        orderRepository.getByDateAndTotalSum(date, sum);

        verify(jdbcTemplate).query(anyString(),
                any(RowMapper.class),
                eq(date),
                eq(sum));
    }

    @Test
    void getByExceptAndPeriodCallsDatabase() {
        String except = "some text";
        LocalDate minDate = LocalDate.of(2020,01, 01);
        LocalDate maxDate = LocalDate.of(2025,12, 31);

        orderRepository.getByExceptAndPeriod(except, minDate, maxDate);

        verify(jdbcTemplate).query(anyString(),
                any(RowMapper.class),
                eq(except),
                eq(minDate),
                eq(maxDate));
    }
}