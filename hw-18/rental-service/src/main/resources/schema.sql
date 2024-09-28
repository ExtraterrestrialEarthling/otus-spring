CREATE TABLE rentals (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        car_id BIGINT NOT NULL,
                        customer_id BIGINT,
                        rental_start_date TIMESTAMP NOT NULL,
                        rental_end_date TIMESTAMP NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        price_total DECIMAL(19, 2) NOT NULL
);
