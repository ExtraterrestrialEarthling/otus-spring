package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.dto.request.CommentDtoRq;
import ru.otus.hw.dto.response.CommentDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final long NON_EXISTING_ID = 999L;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataProvider.getDbAuthors();
        dbGenres = TestDataProvider.getDbGenres();
        dbBooks = TestDataProvider.getDbBooks(dbAuthors, dbGenres);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        when(commentMapper.entityToDtoRs(commentArgumentCaptor.capture())).thenAnswer(invocation -> {
            Comment capturedComment = commentArgumentCaptor.getValue();
            return toCommentDtoRs(capturedComment);
        });
    }

    @Test
    @DisplayName("должен возвращать правильный список комментариев по bookId")
    void shouldReturnCorrectCommentListByBookId(){
        Book book = dbBooks.get(0);
        List<CommentDtoRs> comments = commentService.findByBookId(book.getId());
        assertThat(comments)
                .isEqualTo(List.of(new CommentDtoRs(1L, "nice", book.getId()),
                        new CommentDtoRs(2L, "cool", book.getId())));
    }

    @Test
    @DisplayName("должен возвращать правильный комментарий по id")
    void shouldReturnCorrectCommentById(){
        Book book = dbBooks.get(0);
        long id = 1L;
        CommentDtoRs expectedComment = new CommentDtoRs(id, "nice", book.getId());
        CommentDtoRs actualComment = commentService.findById(id);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно добавлять новый комментарий")
    @DirtiesContext
    void shouldSaveNewComment(){
        Book book = dbBooks.get(1);
        long id = 5L;
        CommentDtoRs expectedComment = new CommentDtoRs(id,"amazing book, love it", book.getId());
        commentService.insert(new CommentDtoRq(expectedComment.getId(),
                expectedComment.getText(), expectedComment.getBookId()));
        CommentDtoRs actualComment = commentService.findById(id);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен корректно обновлять комментарий по id")
    @DirtiesContext
    void shouldUpdateComment(){
        Book book = dbBooks.get(0);
        long commentId = 1L;
        CommentDtoRs expectedComment = new CommentDtoRs(commentId, "bad book", book.getId());
        commentService.update(new CommentDtoRq(expectedComment.getId(),
                expectedComment.getText(), expectedComment.getBookId()));
        CommentDtoRs actualComment = commentService.findById(commentId);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий корректно")
    @DirtiesContext
    void shouldDeleteComment(){
        CommentDtoRs commentForDeletion = commentService.findById(1);
        commentService.deleteById(commentForDeletion.getId());
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(()->
                commentService.findById(commentForDeletion.getId()));
    }

    @Test
    @DisplayName("должен выбрасывать exception, если при обновлении комментария указан неверный id")
    void updateShouldThrowExceptionIfIdDoesntExist() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentService.update(new CommentDtoRq(NON_EXISTING_ID, "doesn't matter", 1L)));
    }


    private CommentDtoRs toCommentDtoRs(Comment comment){
        return new CommentDtoRs(comment.getId(), comment.getText(), comment.getBook().getId());
    }
}
