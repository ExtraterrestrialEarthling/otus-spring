package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;

@Slf4j
@Service
public class OrderValidationServiceImpl implements OrderValidationService {

    @Override
    public Order validate(Order order) {
        log.info("Order validation in process");
        log.info("Order validation success");
        return order;
    }
}
