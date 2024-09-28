package ru.otus.hw.utils;

import java.math.BigDecimal;

public interface PriceCalculator {
    BigDecimal calculatePrice(BigDecimal pricePerHour,
                              Long durationInMinutes);
}
