package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final int EXPECTED_NUM_OF_BOOKS = 3;

    private static final long NOT_EXISTING_ID = 999L;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    @DisplayName("findAll должен возвращать правильную книгу")
    void shouldReturnCorrectBookList(){
        List<Book> expectedBooks = dbBooks;

        List<Book> actualBooks = bookService.findAll();

        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_NUM_OF_BOOKS)
                .allMatch(b -> !b.getTitle().isEmpty(), "Не должно быть пустое название")
                .allMatch(b -> b.getAuthor() != null, "Автор не должен быть null")
                .allMatch(b -> b.getGenres() != null && b.getGenres().size()>0, "У книги должен быть хотя бы один автор");

        assertThat(actualBooks).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBooks);

        for (Book book : actualBooks){
            assertThatNoException().isThrownBy(()-> book.getAuthor().getFullName());
            assertThatNoException().isThrownBy(()-> book.getGenres().size());
        }
    }

    @Test
    @DisplayName("findById должен возвращать правильную книгу")
    void shouldReturnCorrectBook(){
        Book expectedBook = dbBooks.get(0);
        Optional<Book> actualBook = bookService.findById(1);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get()).isEqualTo(expectedBook);
        assertThatNoException().isThrownBy(()-> actualBook.get().getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.get().getGenres().size());
    }

    @Test
    @DisplayName("insert должен правильно сохранять книгу")
    @DirtiesContext
    void shouldSaveBookCorrectly(){
        String bookTitle = "insertedBook";
        Book expectedBook = new Book(0, bookTitle, dbAuthors.get(0), List.of(dbGenres.get(0)));

        bookService.insert(bookTitle, dbAuthors.get(0).getId(), Set.of(dbGenres.get(0).getId()));

        List<Book> bookList = bookService.findAll();
        Book actualBook = bookList.stream().filter(book -> book.getTitle().equals(bookTitle)).findFirst().orElse(null);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThat(actualBook.getAuthor()).isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.getGenres()).containsExactlyInAnyOrderElementsOf(expectedBook.getGenres());

        assertThatNoException().isThrownBy(()-> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.getGenres().size());
    }

    @Test
    @DisplayName("Должен корректно обновлять книгу")
    @DirtiesContext
    void shouldUpdateBookCorrectly(){
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

        assertThatNoException().isThrownBy(()-> actualBook.getAuthor().getFullName());
        assertThatNoException().isThrownBy(()-> actualBook.getGenres().size());
    }

    @Test
    @DisplayName("Метод update должен выбрасывать exception, если такой id не существует")
    void updateShouldThrowExceptionIfIdDoesntExist(){
        Book bookForUpdate = dbBooks.get(2);
        bookForUpdate.setId(NOT_EXISTING_ID);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> bookService.update(bookForUpdate.getId(),
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
    void shouldDeleteBookCorrectly(){
        bookService.deleteById(1L);
        Optional<Book> bookOptional = bookService.findById(1L);
        assertThat(bookOptional).isEmpty();
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
                bookService.insert("newBook", 1, Set.of(NOT_EXISTING_ID)));
    }

    @Test
    @DisplayName("метод должен выбрасывать исключение при попытке" +
            " добавить книгу с несуществующим автором")
    @DirtiesContext
    void insertBookShouldThrowExceptionIfIncorrectAuthor(){
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(()->
                bookService.insert("newBook", NOT_EXISTING_ID, Set.of(1L)));
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
