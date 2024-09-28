package ru.otus.hw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.domain.Car;


@RepositoryRestResource(path = "cars")
public interface CarRepository extends JpaRepository<Car, Long> {

}
