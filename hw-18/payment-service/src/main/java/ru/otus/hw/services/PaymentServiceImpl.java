package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Payment;
import ru.otus.hw.dto.PaymentRequest;
import ru.otus.hw.repo.PaymentRepository;
import ru.otus.hw.utils.PriceCalculator;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PriceCalculator priceCalculator;

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment createPayment(PaymentRequest paymentRequest) {
        return paymentRepository.save(new Payment(null,
                paymentRequest.getCarId(),
                priceCalculator.calculatePrice(BigDecimal.valueOf(paymentRequest.getPricePerHour()),
                        paymentRequest.getDurationInMinutes())
                , true
        ));
    }
}
