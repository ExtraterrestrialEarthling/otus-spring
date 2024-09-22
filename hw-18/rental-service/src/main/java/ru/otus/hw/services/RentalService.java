package ru.otus.hw.services;

import ru.otus.hw.domain.Rental;
import ru.otus.hw.dto.CarRentalDto;
import ru.otus.hw.dto.RentalRequest;

import java.util.List;

public interface RentalService {

    CarRentalDto rentCar(RentalRequest rentalRequest);

    List<Rental> getRentals();
}
