package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {

    @Override
    public Order processPaidOrder(Order order) {
        log.info("Paid order in process");
        log.info("Order processed successfully");
        return order;
    }

    @Override
    public Order processFreeOrder(Order order) {
        log.info("Free order in process");
        log.info("Order processed successfully");
        return order;
    }
}
