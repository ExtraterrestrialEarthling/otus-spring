package ru.otus.hw.dto;

import lombok.Data;

@Data
public class RentalRequest {
    private Long carId;
    private Long durationInMinutes;
}