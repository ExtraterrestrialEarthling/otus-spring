package ru.otus.hw.services;

import ru.otus.hw.domain.Order;

public interface OrderProcessingService {

    Order processPaidOrder(Order order);

    Order processFreeOrder(Order order);
}
