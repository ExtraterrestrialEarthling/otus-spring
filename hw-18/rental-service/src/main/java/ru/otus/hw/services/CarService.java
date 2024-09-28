package ru.otus.hw.services;

import ru.otus.hw.domain.CarVo;

public interface CarService {
    CarVo getAvailableCar(Long carId);
}
