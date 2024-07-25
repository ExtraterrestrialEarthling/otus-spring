package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.config.TestMongoConfig;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataMongoTest
@Import({CommentServiceImpl.class, TestMongoConfig.class})
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    private static final String NOT_EXISTING_ID = "1234";

    @BeforeEach
    void setUp() {
        dbBooks = bookRepository.findAll();
        dbComments = commentRepository.findAll();
    }

    @Test
    @DisplayName("должен возвращать правильный список комментариев по bookId")
    void shouldReturnCorrectCommentListByBookId() {
        Book book = dbBooks.get(0);
        List<Comment> expectedComments = commentRepository.findByBookId(book.getId());
        List<Comment> comments = commentService.findByBookId(book.getId());
        assertThat(comments)
                .isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("должен возвращать правильный комментарий по id")
    void shouldReturnCorrectCommentById() {
        Book book = dbBooks.get(0);
        String id = dbComments.get(0).getId();
        Comment expectedComment = new Comment(id, "nice", book);
        Optional<Comment> actualComment = commentService.findById(id);
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно добавлять новый комментарий")
    @DirtiesContext
    void shouldSaveNewComment() {
        Comment comment = dbComments.get(2);
        Book book = comment.getBook();
        String id = comment.getId();
        Comment expectedComment = new Comment(id, comment.getText(), book);
        commentService.insert(expectedComment.getText(), expectedComment.getBook().getId());
        Optional<Comment> commentOptional = commentService.findById(id);
        assertThat(commentOptional).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно обновлять комментарий по id")
    @DirtiesContext
    void shouldUpdateComment() {
        Comment comment = dbComments.get(1);
        String commentId = comment.getId();
        Comment expectedComment = new Comment(commentId, "bad book", comment.getBook());
        commentService.update(expectedComment.getId(), expectedComment.getText());
        Optional<Comment> commentOptional = commentService.findById(commentId);
        assertThat(commentOptional).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий корректно")
    @DirtiesContext
    void shouldDeleteComment() {
        String commentId = dbComments.get(2).getId();
        commentService.deleteById(commentId);
        assertThat(commentService.findById(commentId)).isEmpty();
    }

    @Test
    @DisplayName("должен выбрасывать exception, если при обновлении комментария указан неверный id")
    void updateShouldThrowExceptionIfIdDoesntExist() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentService.update(NOT_EXISTING_ID, "doesn't matter"));
    }
}
