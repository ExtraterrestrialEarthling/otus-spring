package ru.otus.hw.services;

import ru.otus.hw.domain.Payment;
import ru.otus.hw.dto.PaymentRequest;

import java.util.List;

public interface PaymentService {
    List<Payment> getPayments();

    Payment createPayment(PaymentRequest paymentRequest);
}
