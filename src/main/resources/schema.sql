drop DATABASE IF EXISTS retail_test;
create database retail_test;
USE retail_test;

CREATE TABLE IF NOT EXISTS chains (
    chain_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT unique,
    chain_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT unique,
    product_code INT NOT NULL unique,
    product_desc VARCHAR(255) NOT NULL,
    product_category_code INT NOT NULL,
    product_category_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT unique,
    customer_code INT NOT NULL unique,
    customer_name VARCHAR(255) NOT NULL,
    chain_id INT NOT NULL,
    FOREIGN KEY (chain_id) REFERENCES chains(chain_id)
);

CREATE TABLE IF NOT EXISTS prices (
    price_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT unique,
    product_id INT NOT NULL,
    regular_price DECIMAL(10, 2) NOT NULL,
    chain_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (chain_id) REFERENCES chains(chain_id)
);

CREATE TABLE IF NOT EXISTS shipments (
    shipment_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT unique,
    shipment_date DATE NOT NULL,
    product_id INT NOT NULL,
    customer_id INT NOT NULL,
    chain_id INT NOT NULL,
    volume INT NOT NULL,
    sales_value DECIMAL(10,2) NOT NULL,
    promo_sign ENUM('REGULAR', 'PROMO') NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (product_id),
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (chain_id) REFERENCES chains(chain_id)
);








--
--
--
--
--CREATE TABLE IF NOT EXISTS category (
--    category_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    category_code INT NOT NULL UNIQUE,
--    category_name VARCHAR(255)  NOT NULL
--);
--
--CREATE TABLE IF NOT EXISTS products (
--    product_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    product_code INT NOT NULL,
--    product_desc VARCHAR(255) NOT NULL,
--    product_category VARCHAR(255) NOT NULL,
--    category_name INT NOT NULL
--    FOREIGN KEY (category_id) REFERENCES category (category_id)
--);
--
--CREATE TABLE IF NOT EXISTS customers (
--    customer_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    customer_code INT NOT NULL,
--    address VARCHAR(255) NOT NULL,
--    chain_name VARCHAR(255) NOT NULL
--);
--
--CREATE TABLE IF NOT EXISTS prices (
--    price_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    product_code INT NOT NULL,
--    regular_price DECIMAL(10, 2) NOT NULL
--    FOREIGN KEY (product_id) REFERENCES products(product_id)
--);
--
--CREATE TABLE IF NOT EXISTS shipments (
--    shipment_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    shipment_date DATE NOT NULL,
--    product_code VARCHAR(255) NOT NULL,
--    address VARCHAR(255) NOT NULL,
--    chain VARCHAR(255) NOT NULL,
--    volume INT NOT NULL,
--    sales_value DECIMAL(10,2) NOT NULL,
--    promo_sign ENUM('REGULAR', 'PROMO') NOT NULL
--    FOREIGN KEY (product_id) REFERENCES products (product_id),
--    FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
--);



