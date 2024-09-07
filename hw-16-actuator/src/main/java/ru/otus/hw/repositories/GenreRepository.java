package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource(path = "genres")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @NonNull
    List<Genre> findAll();

    List<Genre> findByIdIn(Set<Long> ids);

    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

}
