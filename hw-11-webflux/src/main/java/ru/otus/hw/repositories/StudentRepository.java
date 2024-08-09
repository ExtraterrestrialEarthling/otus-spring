package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Student;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

    Mono<Student> findById(String id);

    @NonNull
    Flux<Student> findAll();

    Mono<Void> deleteById(String id);
}
