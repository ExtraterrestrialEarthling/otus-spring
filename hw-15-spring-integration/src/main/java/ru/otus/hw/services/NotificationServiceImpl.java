package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public Order sendNotification(Order order) {
        log.info("Notification sent");
        return order;
    }
}
