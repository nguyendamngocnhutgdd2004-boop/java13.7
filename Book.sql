CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

DROP TABLE IF EXISTS Book;
CREATE TABLE Book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_year INT NOT NULL, -- Thay kiểu YEAR bằng INT để đồng bộ mượt mà với Java
    price DECIMAL(10, 2) NOT NULL
);
