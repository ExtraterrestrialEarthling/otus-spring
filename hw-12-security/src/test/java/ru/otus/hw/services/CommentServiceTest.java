package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final long NOT_EXISTING_ID = 999L;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    @DisplayName("должен возвращать правильный список комментариев по bookId")
    void shouldReturnCorrectCommentListByBookId(){
        Book book = dbBooks.get(0);
        List<Comment> comments = commentService.findByBookId(book.getId());
        assertThat(comments)
                .isEqualTo(List.of(new Comment(1L, "nice", book),
                        new Comment(2L, "cool", book)));
    }

    @Test
    @DisplayName("должен возвращать правильный комментарий по id")
    void shouldReturnCorrectCommentById(){
        Book book = dbBooks.get(0);
        long id = 1L;
        Comment expectedComment = new Comment(id, "nice", book);
        Optional<Comment> actualComment = commentService.findById(id);
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно добавлять новый комментарий")
    @DirtiesContext
    void shouldSaveNewComment(){
        Book book = dbBooks.get(1);
        long id = 5L;
        Comment expectedComment = new Comment(id,"amazing book, love it", book);
        commentService.insert(expectedComment.getText(), expectedComment.getBook().getId());
        Optional<Comment> commentOptional = commentService.findById(id);
        assertThat(commentOptional).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно обновлять комментарий по id")
    @DirtiesContext
    void shouldUpdateComment(){
        Book book = dbBooks.get(0);
        long commentId = 1L;
        Comment expectedComment = new Comment(commentId, "bad book", book);
        commentService.update(expectedComment.getId(), expectedComment.getText());
        Optional<Comment> commentOptional = commentService.findById(commentId);
        assertThat(commentOptional).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий корректно")
    @DirtiesContext
    void shouldDeleteComment(){
        Comment commentForDeletion = commentService.findById(1).get();
        commentService.deleteById(commentForDeletion.getId());
        assertThat(commentService.findById(commentForDeletion.getId())).isEmpty();
    }

    @Test
    @DisplayName("должен выбрасывать exception, если при обновлении комментария указан неверный id")
    void updateShouldThrowExceptionIfIdDoesntExist() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentService.update(NOT_EXISTING_ID, "doesn't matter"));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
