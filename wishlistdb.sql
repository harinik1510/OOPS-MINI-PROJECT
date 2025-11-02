CREATE DATABASE wishlistdb;
USE wishlistdb;
CREATE TABLE wishlist (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    model VARCHAR(50),
    price DOUBLE
);
