package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.dto.request.BookDtoRq;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.dto.response.BookDtoRs;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final int EXPECTED_NUM_OF_BOOKS = 3;

    private static final long NOT_EXISTING_ID = 999L;


    @BeforeEach
    void setUp() {
        dbAuthors = TestDataProvider.getDbAuthors();
        dbGenres = TestDataProvider.getDbGenres();
        dbBooks = TestDataProvider.getDbBooks(dbAuthors, dbGenres);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        when(bookMapper.entityToDtoRs(bookArgumentCaptor.capture())).thenAnswer(invocation -> {
            Book capturedBook = bookArgumentCaptor.getValue();
            return toBookDtoRs(capturedBook);
        });
    }

    @Test
    @DisplayName("findAll должен возвращать правильную книгу")
    void shouldReturnCorrectBookList(){
        List<BookDtoRs> expectedBooks = dbBooks.stream().map(this::toBookDtoRs).toList();
        List<BookDtoRs> actualBooks = bookService.findAll();

        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUM_OF_BOOKS)
                .allMatch(b -> !b.getTitle().isEmpty(), "Не должно быть пустое название")
                .allMatch(b -> b.getAuthor() != null, "Автор не должен быть null")
                .allMatch(b -> b.getGenres() != null && b.getGenres().size()>0, "У книги должен быть хотя бы один автор");

        assertThat(actualBooks).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBooks);

        for (BookDtoRs book : actualBooks){
            assertThatNoException().isThrownBy(()-> book.getAuthor().getFullName());
            assertThatNoException().isThrownBy(()-> book.getGenres().size());
        }
    }

    @Test
    @DisplayName("findById должен возвращать правильную книгу")
    void shouldReturnCorrectBook(){
        BookDtoRs expectedBook = toBookDtoRs(dbBooks.get(0));
        BookDtoRs actualBook = bookService.findById(1);

        assertThat(actualBook).isEqualTo(expectedBook);
        assertThatNoException().isThrownBy(()-> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.getGenres().size());
    }

    @Test
    @DisplayName("insert должен правильно сохранять книгу")
    @DirtiesContext
    void shouldSaveBookCorrectly(){
        String bookTitle = "insertedBook";
        Book expectedBook = new Book(null, bookTitle, dbAuthors.get(0), List.of(dbGenres.get(0)));
        BookDtoRs expectedBookDto = toBookDtoRs(expectedBook);

        bookService.insert(toBookDtoRq(expectedBook));

        List<BookDtoRs> bookList = bookService.findAll();
        BookDtoRs actualBook = bookList.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getTitle()).isEqualTo(expectedBookDto.getTitle());
        assertThat(actualBook.getAuthor()).isEqualTo(expectedBookDto.getAuthor());
        assertThat(actualBook.getGenres()).containsExactlyInAnyOrderElementsOf(expectedBookDto.getGenres());

        assertThatNoException().isThrownBy(()-> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.getGenres().size());
    }

    @Test
    @DisplayName("Должен корректно обновлять книгу")
    @DirtiesContext
    void shouldUpdateBookCorrectly(){
        Book expectedBook = dbBooks.get(2);
        expectedBook.setTitle("modified title");

        bookService.update(toBookDtoRq(expectedBook));

        BookDtoRs actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(toBookDtoRs(expectedBook));

        assertThatNoException().isThrownBy(()-> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.getGenres().size());
    }

    @Test
    @DisplayName("Метод update должен выбрасывать exception, если такой id не существует")
    void updateShouldThrowExceptionIfIdDoesntExist(){
        Book bookForUpdate = dbBooks.get(2);
        bookForUpdate.setId(NOT_EXISTING_ID);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> bookService.update(toBookDtoRq(bookForUpdate)));
    }

    @Test
    @DisplayName("Должен корректно удалять книгу")
    @DirtiesContext
    void shouldDeleteBookCorrectly(){
        bookService.deleteById(1L);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(()->
                bookService.findById(1L));
    }

    @Test
    @DisplayName("deleteById должен возвращать exception если id не существует")
    void deleteBookShouldThrowExceptionIfIdDoesntExist(){
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> bookService.deleteById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("метод должен выбрасывать исключение при попытке" +
            " добавить книгу с несуществующим жанром")
    @DirtiesContext
    void insertBookShouldThrowExceptionIfIncorrectGenre(){
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(()->
                bookService.insert(new BookDtoRq(null, "newBook", 1L, Set.of(NOT_EXISTING_ID))));
    }

    @Test
    @DisplayName("метод должен выбрасывать исключение при попытке" +
            " добавить книгу с несуществующим автором")
    @DirtiesContext
    void insertBookShouldThrowExceptionIfIncorrectAuthor(){
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(()->
                bookService.insert(new BookDtoRq(null, "newBook", NOT_EXISTING_ID, Set.of(1L))));
    }

    private BookDtoRs toBookDtoRs(Book book){
        return new BookDtoRs(book.getId(), book.getTitle(),
                new AuthorDtoRs(book.getAuthor().getId(), book.getAuthor().getFullName()),
                book.getGenres().stream().map(genre -> new GenreDtoRs(genre.getId(), genre.getName())).toList());
    }

    private BookDtoRq toBookDtoRq(Book book){
        return new BookDtoRq(book.getId(), book.getTitle(), book.getAuthor().getId(),
                book.getGenres()
                        .stream()
                        .map(Genre::getId).collect(Collectors.toSet()));
    }
}
