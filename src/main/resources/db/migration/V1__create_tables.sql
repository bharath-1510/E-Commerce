-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
);

-- Create addresses table
CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    street VARCHAR(255),
    city VARCHAR(100),
    phone_number VARCHAR(20),
    postal_code VARCHAR(20),
    country VARCHAR(100)
);


-- Create Token Table

CREATE TABLE token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    revoked BOOLEAN,
    expired BOOLEAN,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);

-- Create Product table
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status BOOLEAN,
    type VARCHAR(50),
    code VARCHAR(255) UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create ProductVariant table
CREATE TABLE product_variant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
    sku VARCHAR(50),
    price DOUBLE PRECISION,
    stock_quantity BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create ProductOption table
CREATE TABLE product_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    variant_id BIGINT REFERENCES product_variant(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
    value VARCHAR(255),
    category VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);



-- Create Discount table
CREATE TABLE discount (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    discount_type VARCHAR(50) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    usage_limit INTEGER,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT chk_discount_type CHECK (discount_type IN ('FREE_SHIPPING','PERCENTAGE','FIXED_AMOUNT')) -- Enum constraint
);

-- Create Cart table
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create CartItem table
CREATE TABLE cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT REFERENCES cart(id) ON DELETE CASCADE,
    variant_id BIGINT REFERENCES product_variant(id) ON DELETE CASCADE,
    quantity INTEGER,
    price DOUBLE PRECISION
);
