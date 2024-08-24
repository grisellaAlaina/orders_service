INSERT INTO user_order(order_number, total_sum, order_date, recipient, delivery_address, payment_type, delivery_type)
VALUES ('Order1', 1000, CURRENT_DATE, 'Recipient1', 'Address1', 'PaymentType1', 'DeliveryType1'),
       ('Order2', 2000, CURRENT_DATE, 'Recipient2', 'Address2', 'PaymentType2', 'DeliveryType2');

INSERT INTO order_detail(product_art, product_name, quantity, product_price, order_id)
VALUES (1, 'Product1', 1, 100, (SELECT id FROM user_order WHERE order_number = 'Order1')),
       (2, 'Product2', 2, 200, (SELECT id FROM user_order WHERE order_number = 'Order2'));