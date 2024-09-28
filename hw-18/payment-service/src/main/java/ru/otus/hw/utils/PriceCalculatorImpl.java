package ru.otus.hw.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PriceCalculatorImpl implements PriceCalculator {

    @Override
    public BigDecimal calculatePrice(BigDecimal pricePerHour,
                                     Long durationInMinutes) {

        return pricePerHour.multiply(BigDecimal.valueOf(durationInMinutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
