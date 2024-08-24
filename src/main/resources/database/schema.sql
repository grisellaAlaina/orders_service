CREATE TABLE user_order
(
    id               SERIAL PRIMARY KEY,
    order_number     TEXT,
    total_sum        INT,
    order_date       DATE,
    recipient        TEXT,
    delivery_address TEXT,
    payment_type     TEXT,
    delivery_type    TEXT
);

CREATE TABLE order_detail
(
    id            SERIAL PRIMARY KEY,
    product_art   INT,
    product_name  TEXT,
    quantity      INT,
    product_price INT,
    order_id      INT,
    FOREIGN KEY (order_id) REFERENCES user_order (id)
);