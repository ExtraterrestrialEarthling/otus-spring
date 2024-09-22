package ru.otus.hw.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long carId;

    private Double pricePerHour;

    private Long durationInMinutes;
}
