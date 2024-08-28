package ru.otus.hw.repositories.nosql;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.nosql.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

    @NonNull
    List<Author> findAll();

    Optional<Author> findById(String id);
}
