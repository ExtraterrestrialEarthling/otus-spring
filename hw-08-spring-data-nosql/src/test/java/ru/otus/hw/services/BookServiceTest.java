package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final int EXPECTED_NUM_OF_BOOKS = 3;

    private static final String NOT_EXISTING_ID = "1234";


    @BeforeEach
    void setUp() {
        dbAuthors = authorRepository.findAll();
        dbGenres = genreRepository.findAll();
        dbBooks = bookRepository.findAll();
    }

    @Test
    @DisplayName("findAll должен возвращать правильную книгу")
    void shouldReturnCorrectBookList() {
        List<Book> expectedBooks = dbBooks;

        List<Book> actualBooks = bookService.findAll();

        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUM_OF_BOOKS)
                .allMatch(b -> !b.getTitle().isEmpty(), "Не должно быть пустое название")
                .allMatch(b -> b.getAuthor() != null, "Автор не должен быть null")
                .allMatch(b -> b.getGenres() != null && b.getGenres().size() > 0, "У книги должен быть хотя бы один автор");

        assertThat(actualBooks).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBooks);
    }

    @Test
    @DisplayName("findById должен возвращать правильную книгу")
    void shouldReturnCorrectBook() {
        Book expectedBook = dbBooks.get(0);
        String expectedBookId = expectedBook.getId();
        Optional<Book> actualBook = bookService.findById(expectedBookId);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get()).isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("insert должен правильно сохранять книгу")
    @DirtiesContext
    void shouldSaveBookCorrectly() {
        String bookTitle = "insertedBook";
        Author bookAuthor = dbAuthors.get(0);
        List<Genre> bookGenres = List.of(dbGenres.get(1));
        Book expectedBook = new Book(null, bookTitle, bookAuthor, bookGenres);

        bookService.insert(bookTitle, bookAuthor.getId(),
                bookGenres.stream().map(Genre::getId).collect(Collectors.toSet()));

        List<Book> bookList = bookService.findAll();
        Book actualBook = bookList.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThat(actualBook.getAuthor()).isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.getGenres()).containsExactlyInAnyOrderElementsOf(expectedBook.getGenres());
    }

    @Test
    @DisplayName("Должен корректно обновлять книгу")
    @DirtiesContext
    void shouldUpdateBookCorrectly() {
        Book expectedBook = dbBooks.get(2);
        expectedBook.setTitle("modified title");
        bookService.update(expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .collect(Collectors.toSet()));
        Optional<Book> actualBookOptional = bookService.findById(expectedBook.getId());
        assertThat(actualBookOptional).isPresent();
        Book actualBook = actualBookOptional.get();
        assertThat(actualBook).isEqualTo(expectedBook);
    }


    @Test
    @DisplayName("Метод update должен выбрасывать exception, если такой id не существует")
    void updateShouldThrowExceptionIfIdDoesntExist() {
        Book bookForUpdate = dbBooks.get(2);
        bookForUpdate.setId(NOT_EXISTING_ID);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.update(bookForUpdate.getId(),
                        bookForUpdate.getTitle(),
                        bookForUpdate.getAuthor().getId(),
                        bookForUpdate.getGenres()
                                .stream()
                                .map(Genre::getId)
                                .collect(Collectors.toSet())));
    }

    @Test
    @DisplayName("Должен корректно удалять книгу")
    @DirtiesContext
    void shouldDeleteBookCorrectly() {
        String id = dbBooks.get(1).getId();
        bookService.deleteById(id);
        Optional<Book> bookOptional = bookService.findById(id);
        assertThat(bookOptional).isEmpty();
    }

    @Test
    @DisplayName("deleteById должен возвращать exception если id не существует")
    void deleteBookShouldThrowExceptionIfIdDoesntExist() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.deleteById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("метод должен выбрасывать исключение при попытке" +
            " добавить книгу с несуществующим жанром")
    @DirtiesContext
    void insertBookShouldThrowExceptionIfIncorrectGenre() {
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                bookService.insert("newBook", dbAuthors.get(0).getId(),
                        Set.of(NOT_EXISTING_ID)));
    }

    @Test
    @DisplayName("метод должен выбрасывать исключение при попытке" +
            " добавить книгу с несуществующим автором")
    @DirtiesContext
    void insertBookShouldThrowExceptionIfIncorrectAuthor() {
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                bookService.insert("newBook", NOT_EXISTING_ID, Set.of(dbGenres.get(0).getId())));
    }
}
