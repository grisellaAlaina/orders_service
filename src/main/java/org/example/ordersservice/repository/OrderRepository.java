package org.example.ordersservice.repository;

import org.example.ordersservice.dto.OrderResponseDto;
import org.example.ordersservice.model.UserOrder;
import org.example.ordersservice.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserOrder createOrder(UserOrder order) {
        String sql =
                "INSERT INTO user_order(order_number, total_sum, order_date, recipient, delivery_address, payment_type, delivery_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, order.getOrderNumber());
                    ps.setInt(2, order.getTotalSum());
                    ps.setObject(3, order.getOrderDate(), Types.DATE);
                    ps.setString(4, order.getRecipient());
                    ps.setString(5, order.getDeliveryAddress());
                    ps.setString(6, order.getPaymentType());
                    ps.setString(7, order.getDeliveryType());
                    return ps;
                },
                keyHolder);

        if (keyHolder.getKey() != null) {
            order.setId(keyHolder.getKey().intValue());
        }
        return order;
    }

    public OrderDetail createOrderDetail(OrderDetail detail) {
        String sql =
                "INSERT INTO order_detail(product_art, product_name, quantity, product_price, order_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, detail.getProductArt());
                    ps.setString(2, detail.getProductName());
                    ps.setInt(3, detail.getQuantity());
                    ps.setInt(4, detail.getProductPrice());
                    ps.setInt(5, detail.getOrderId());
                    return ps;
                },
                keyHolder);

        if (keyHolder.getKey() != null) {
            detail.setId(keyHolder.getKey().intValue());
        }
        return detail;
    }

    public OrderResponseDto getById(int id) {
        String select = "SELECT * FROM user_order u " +
                "LEFT JOIN order_detail o " +
                "ON u.id = o.order_id WHERE u.id = ?";
        return jdbcTemplate.queryForObject(select,
                new BeanPropertyRowMapper<>(OrderResponseDto.class),
                id
        );
    }

    public List<OrderResponseDto> getByDateAndTotalSum(LocalDate date, int sum) {
        String select = "SELECT * FROM user_order u " +
                "LEFT JOIN order_detail o " +
                "ON u.id = o.order_id " +
                "WHERE u.order_date = ? " +
                "AND u.total_sum = ?";
        return jdbcTemplate.query(select,
                new BeanPropertyRowMapper<>(OrderResponseDto.class),
                date, sum);
    }

    public List<OrderResponseDto> getByExceptAndPeriod(String except, LocalDate minDate, LocalDate maxDate) {
        String select = "SELECT * FROM user_order u " +
                "LEFT JOIN order_detail o " +
                "ON u.id = o.order_id " +
                "WHERE o.product_name != ? " +
                "AND u.order_date > ? AND u.order_date < ?";
        return jdbcTemplate.query(select, new BeanPropertyRowMapper<>(OrderResponseDto.class),
                except, minDate, maxDate);
    }
}