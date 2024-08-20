package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"genres", "author"})
    Optional<Book> findById(long id);

    @NonNull
    @EntityGraph(attributePaths = {"genres", "author"})
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    void deleteById(long id);
}
