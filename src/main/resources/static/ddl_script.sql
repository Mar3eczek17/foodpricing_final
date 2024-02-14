CREATE DATABASE IF NOT EXISTS food_price_monitoring COLLATE = utf8_general_ci;

USE food_price_monitoring;

CREATE TABLE user (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_role ENUM('ADMIN', 'USER') NOT NULL
);

CREATE TABLE product_category (
    category_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    category_name ENUM('FRUITS', 'VEGETABLES', 'DAIRY', 'MEAT', 'BEVERAGES') NOT NULL UNIQUE
);

CREATE TABLE trading_point (
    point_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    point_name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(255)
);

CREATE TABLE product (
    product_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES product_category(category_id)
);

CREATE TABLE price_tracking (
    tracking_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    product_id INTEGER,
    point_id INTEGER,
    price DOUBLE(10, 2),
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (point_id) REFERENCES trading_point(point_id)
);