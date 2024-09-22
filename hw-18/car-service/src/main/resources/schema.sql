CREATE TABLE cars (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     brand VARCHAR(255) NOT NULL,
                     model VARCHAR(255) NOT NULL,
                     year_of_manufacture INT NOT NULL,
                     color VARCHAR(255),
                     price_per_hour DOUBLE NOT NULL,
                     mileage INT NOT NULL,
                     status VARCHAR(255)
);
