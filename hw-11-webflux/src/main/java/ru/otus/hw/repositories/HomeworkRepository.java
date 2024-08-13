package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Homework;

public interface HomeworkRepository extends ReactiveMongoRepository<Homework, String> {

    Mono<Homework> findById(String id);

    Flux<Homework> findByStudentId(String bookId);

    Mono<Void> deleteById(String id);
}
