package ru.otus.hw.services;

import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Receipt;

public interface PaymentService {

    Order processPayment(Order order);

    Receipt getReceipt(Order order);
}
