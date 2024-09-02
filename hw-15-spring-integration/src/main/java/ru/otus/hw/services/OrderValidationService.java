package ru.otus.hw.services;

import ru.otus.hw.domain.Order;

public interface OrderValidationService {

    Order validate(Order order);
}
