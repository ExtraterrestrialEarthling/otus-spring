package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Skill;

import java.util.Set;

public interface SkillRepository extends ReactiveMongoRepository<Skill, String> {

    @NonNull
    Flux<Skill> findAll();

    Flux<Skill> findByIdIn(Set<String> ids);

    Mono<Skill> findById(String id);

    Mono<Skill> findByName(String name);

}
