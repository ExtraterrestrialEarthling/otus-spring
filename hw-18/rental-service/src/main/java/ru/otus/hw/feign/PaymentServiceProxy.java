package ru.otus.hw.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.domain.PaymentVo;
import ru.otus.hw.dto.PaymentRequest;

@FeignClient(name = "payment-service")
public interface PaymentServiceProxy {

    @PostMapping("/api/payments")
    PaymentVo createPayment(@RequestBody PaymentRequest paymentRequest);
}
