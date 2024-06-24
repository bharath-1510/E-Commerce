
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','USER') NOT NULL,
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE address (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    street VARCHAR(255),
    phone_number VARCHAR(255),
    city VARCHAR(255),
    postal_code VARCHAR(255),
    country VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE token (
    id INT NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE,
    revoked TINYINT(1) DEFAULT 0,
    expired TINYINT(1) DEFAULT 0,
    user_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);



CREATE TABLE Product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    status BOOLEAN,
    type VARCHAR(255),
    code VARCHAR(255) UNIQUE,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
);
CREATE TABLE Product_Variant (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    sku VARCHAR(255),
    price DOUBLE,
    stock_quantity BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

CREATE TABLE Product_Option (
    id BIGINT NOT NULL AUTO_INCREMENT,
    variant_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    value VARCHAR(255),
    category VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (variant_id) REFERENCES Product_Variant(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);



CREATE TABLE Discount (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    discount_type ENUM('FREE_SHIPPING','PERCENTAGE','FIXED_AMOUNT'),
    value DOUBLE DEFAULT 0,
    usage_limit INT,
    expires_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id)
);



CREATE TABLE Cart (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Cart_Item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    variant_id BIGINT NOT NULL,
    quantity INT,
    price DOUBLE,
    PRIMARY KEY (id),
    FOREIGN KEY (cart_id) REFERENCES Cart(id),
    FOREIGN KEY (variant_id) REFERENCES Product_Variant(id)
);

