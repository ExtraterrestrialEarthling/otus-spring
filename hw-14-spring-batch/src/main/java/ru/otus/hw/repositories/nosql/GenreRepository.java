package ru.otus.hw.repositories.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.nosql.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findByIdIn(Set<String> ids);
}
