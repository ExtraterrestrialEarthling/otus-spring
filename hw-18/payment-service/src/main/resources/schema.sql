CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          car_id BIGINT NOT NULL,
                          cost DECIMAL(19, 2) NOT NULL,
                          successful BOOLEAN NOT NULL
);
