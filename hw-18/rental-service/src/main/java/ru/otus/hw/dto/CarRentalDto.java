package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.hw.domain.CarVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRentalDto {

    private Long id;

    private CarVo car;

    private Long customerId;

    private LocalDateTime rentalStartDate;

    private LocalDateTime rentalEndDate;

    private String status;

    private BigDecimal priceTotal;
}
