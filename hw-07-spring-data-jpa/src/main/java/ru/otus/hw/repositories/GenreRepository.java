package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @NonNull
    List<Genre> findAll();

    @Query("select g from Genre g where g.id in :ids")
    List<Genre> findAllByIds(@Param("ids") Set<Long> ids);
}
