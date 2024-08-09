package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Teacher;

public interface TeacherRepository extends ReactiveMongoRepository<Teacher, String> {

    @NonNull
    Flux<Teacher> findAll();

    Mono<Teacher> findById(String id);
}
