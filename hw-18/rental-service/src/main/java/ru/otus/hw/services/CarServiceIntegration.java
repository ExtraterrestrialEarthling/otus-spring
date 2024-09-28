package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.CarVo;
import ru.otus.hw.feign.CarServiceProxy;

@Service
@RequiredArgsConstructor
public class CarServiceIntegration implements CarService {

    private final CarServiceProxy carServiceProxy;

    @Override
    public CarVo getAvailableCar(Long carId) {
        CarVo car = carServiceProxy.findCarById(carId);
        if (!car.getStatus().equals("available")) {
            throw new RuntimeException("This car is not available");
        }
        return car;
    }
}
