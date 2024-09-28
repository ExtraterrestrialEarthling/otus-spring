package ru.otus.hw.services;

import ru.otus.hw.domain.PaymentVo;
import ru.otus.hw.dto.PaymentRequest;

public interface PaymentService {
    PaymentVo createPayment(PaymentRequest paymentRequest);

    PaymentVo fallbackCreatePayment(PaymentRequest paymentRequest, Throwable throwable);
}
