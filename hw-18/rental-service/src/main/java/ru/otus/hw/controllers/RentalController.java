package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.domain.Rental;
import ru.otus.hw.dto.CarRentalDto;
import ru.otus.hw.dto.RentalRequest;
import ru.otus.hw.services.RentalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/api/rentals")
    public CarRentalDto rentCar(@RequestBody RentalRequest rentalRequest) {
        return rentalService.rentCar(rentalRequest);
    }
    
    @GetMapping("/api/rentals")
    public List<Rental> getRentals() {
        return rentalService.getRentals();
    }
}
