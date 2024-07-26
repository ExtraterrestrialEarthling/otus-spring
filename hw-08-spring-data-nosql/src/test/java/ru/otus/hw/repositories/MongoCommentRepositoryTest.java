package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mongo репозиторий для работы с комментариями")
@DataMongoTest
public class MongoCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Должен сохранять комментарий")
    @Test
    public void shouldSaveComment() {
        Book book = getTestBook();
        bookRepository.save(book);
        Comment comment = new Comment(null, "excellent book", book);
        comment = commentRepository.save(comment);
        assertThat(comment.getId()).isNotNull();
    }

    @DisplayName("Должен возвращать комментарий по id")
    @Test
    public void shouldReturnCommentById() {
        Book book = getTestBook();
        bookRepository.save(book);
        Comment comment = new Comment(null, "excellent book", book);
        comment = commentRepository.save(comment);
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());
        assertThat(foundComment).isPresent().get().isEqualTo(comment);
    }

    @DisplayName("Должен возвращать все комментарии по bookId")
    @Test
    public void shouldFindAllCommentsByBookId() {
        Book book = bookRepository.save(getTestBook());
        Comment comment1 = new Comment(null, "excellent book", book);
        comment1 = commentRepository.save(comment1);
        Comment comment2 = new Comment(null, "bad book", book);
        comment2 = commentRepository.save(comment2);
        assertThat(commentRepository.findByBookId(book.getId())).isEqualTo(List.of(comment1, comment2));
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    public void shouldDeleteCommentById() {
        Book book = bookRepository.save(getTestBook());
        Comment comment = commentRepository.save(new Comment(null, "excellent book", book));
        assertThat(comment.getId()).isNotNull();
        commentRepository.deleteById(comment.getId());
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    private Book getTestBook() {
        return new Book(null, "Interesting book", new Author(null, "Some author"),
                List.of(new Genre(null, "some genre")));
    }
}
