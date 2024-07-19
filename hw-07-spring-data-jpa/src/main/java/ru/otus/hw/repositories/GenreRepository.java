package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @NonNull
    List<Genre> findAll();

    List<Genre> findByIdIn(Set<Long> ids);
}
