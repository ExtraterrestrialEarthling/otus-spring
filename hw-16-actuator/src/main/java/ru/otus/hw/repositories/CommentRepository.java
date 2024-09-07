package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "book")
    Optional<Comment> findById(long id);

    @EntityGraph(attributePaths = "book")
    List<Comment> findByBookId(long bookId);

    void deleteById(long id);
}
