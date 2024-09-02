package ru.otus.hw.services;

import ru.otus.hw.domain.Order;

public interface NotificationService {

    Order sendNotification(Order order);
}
