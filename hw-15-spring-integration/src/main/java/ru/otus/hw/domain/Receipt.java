package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Receipt {

    private Order order;

    private BigDecimal priceTotal;

    private LocalDateTime localDateTime;

    @Override
    public String toString() {
        return "Receipt{" +
                "purchased items=" + order.getItems().stream()
                .map(item -> item.getName() + ": " + item.getPrice())
                .collect(Collectors.joining(", ")) +
                ", total=" + priceTotal +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
