
CREATE TABLE fulfillment_provider (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE provider_option (
    id BIGINT NOT NULL AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    `option` VARCHAR(255),
    value VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (provider_id) REFERENCES fulfillment_provider(id)
);

CREATE TABLE region (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),
    currency_code VARCHAR(10) NOT NULL,
    tax_rate DOUBLE DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
);


CREATE TABLE shipping_option (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),
    amount DOUBLE,
    is_return BOOLEAN,
    region_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (provider_id) REFERENCES fulfillment_provider(id),
    FOREIGN KEY (region_id) REFERENCES region(id)
);