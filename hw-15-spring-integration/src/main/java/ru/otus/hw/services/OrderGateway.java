package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Receipt;

@MessagingGateway
public interface OrderGateway {

    @Gateway(requestChannel = "ordersChannel", replyChannel = "receiptsChannel")
    Receipt process(Order order);
}
