CREATE TABLE IF NOT EXISTS shipping (
    id BIGINT NOT NULL AUTO_INCREMENT,
    delivery_status BOOLEAN,
    shipping_option_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (shipping_option_id) REFERENCES shipping_option(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    total_price DOUBLE,
    order_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    shipping_id BIGINT NOT NULL,
    discount_id BIGINT,
    address_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (shipping_id) REFERENCES shipping(id),
    FOREIGN KEY (address_id) REFERENCES address(id),
    FOREIGN KEY (discount_id) REFERENCES discount(id)
);

CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255),
    order_id BIGINT NOT NULL,
    quantity INT,
    price DOUBLE DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);



