package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderType;
import ru.otus.hw.domain.Receipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Order processPayment(Order order) {
        log.info("Payment in process");
        log.info("Payment success");
        return order;
    }

    @Override
    public Receipt getReceipt(Order order) {
        BigDecimal total;
        if (order.getOrderType() == OrderType.FREE) {
            total = BigDecimal.ZERO;
        } else {
            total = calculatePriceTotal(order.getItems());
        }
        return new Receipt(order, total, LocalDateTime.now());
    }

    private BigDecimal calculatePriceTotal(List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
