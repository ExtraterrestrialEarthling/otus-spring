package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.PaymentVo;
import ru.otus.hw.dto.PaymentRequest;
import ru.otus.hw.feign.PaymentServiceProxy;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceIntegration implements PaymentService {

    private final PaymentServiceProxy paymentServiceProxy;

    @Override
    @CircuitBreaker(name = "PAYMENT-SERVICE", fallbackMethod = "fallbackCreatePayment")
    @Retry(name = "PAYMENT-SERVICE")
    public PaymentVo createPayment(PaymentRequest paymentRequest) {
        log.info("Trying to create payment...");
        return paymentServiceProxy.createPayment(paymentRequest);
    }

    @Override
    public PaymentVo fallbackCreatePayment(PaymentRequest paymentRequest, Throwable throwable) {
        log.info("Payment service unavailable: {}", throwable.getMessage());
        return new PaymentVo(BigDecimal.ZERO, "Failed to submit payment", false);
    }
}
