package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.CarVo;
import ru.otus.hw.domain.PaymentVo;
import ru.otus.hw.domain.Rental;
import ru.otus.hw.dto.CarRentalDto;
import ru.otus.hw.dto.PaymentRequest;
import ru.otus.hw.dto.RentalRequest;
import ru.otus.hw.repo.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public CarRentalDto rentCar(RentalRequest rentalRequest) {
        Long carId = rentalRequest.getCarId();
        CarVo car = carService.getAvailableCar(carId);
        PaymentVo payment = paymentService.createPayment(
                new PaymentRequest(carId, car.getPricePerHour(),
                        rentalRequest.getDurationInMinutes()));
        Rental rental = saveRental(carId, payment, rentalRequest);
        return buildCarRentalDto(car, rental);
    }

    private Rental saveRental(Long carId, PaymentVo payment, RentalRequest rentalRequest) {
        String status = payment.isSuccessful() ? "active" : "failed";
        Rental rental = Rental.builder()
                .rentalStartDate(LocalDateTime.now())
                .rentalEndDate(LocalDateTime.now().plusMinutes(rentalRequest.getDurationInMinutes()))
                .carId(carId)
                .status(status)
                .priceTotal(payment.getCost())
                .build();
        return rentalRepository.save(rental);
    }

    private CarRentalDto buildCarRentalDto(CarVo car, Rental rental) {
        return CarRentalDto.builder()
                .car(car)
                .rentalStartDate(rental.getRentalStartDate())
                .rentalEndDate(rental.getRentalEndDate())
                .status(rental.getStatus())
                .build();
    }

    @Override
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkRentalsStatus() {
        List<Rental> rentals = rentalRepository.findAllByStatus("active");
        LocalDateTime now = LocalDateTime.now();
        rentals.stream()
                .filter(rental -> rental.getRentalEndDate().isBefore(now))
                .forEach(rental -> {
                    rental.setStatus("finished");
                    rentalRepository.save(rental);
                });
    }
}
