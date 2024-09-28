package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentVo {

    private BigDecimal cost;

    private String details;

    private boolean successful;
}
