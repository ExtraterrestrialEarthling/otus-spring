package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.Payment;
import ru.otus.hw.dto.PaymentRequest;
import ru.otus.hw.services.PaymentServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/api/payments")
    public List<Payment> getPayments() {
        return paymentService.getPayments();
    }

    @PostMapping("/api/payments")
    public Payment createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest);
    }
}
