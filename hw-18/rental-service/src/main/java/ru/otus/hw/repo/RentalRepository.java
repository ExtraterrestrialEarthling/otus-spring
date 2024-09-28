package ru.otus.hw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.domain.Rental;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findAllByStatus(String status);
}
