package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaBookRepositoryTest {

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private static final int EXPECTED_NUM_OF_BOOKS = 3;

    private static final long EXPECTED_NUM_OF_QUERIES = 1;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    @Order(1)
    void shouldReturnCorrectBooksList() {
        SessionFactory sessionFactory = em.getEntityManager()
                .getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var books = repositoryJpa.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUM_OF_BOOKS)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && b.getGenres().size()>0);
        assertThat(sessionFactory.getStatistics()
                .getPrepareStatementCount())
                .isEqualTo(EXPECTED_NUM_OF_QUERIES);
    }

    @Test
    @DisplayName("должен загружать книгу по id")
    @Order(2)
    void shouldReturnCorrectBookById() {
        var actualBook = repositoryJpa.findById(1L);
        var expectedBook = em.find(Book.class, 1L);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен возращать пустой результат, если книга не найдена")
    @Test
    @Order(3)
    void shouldReturnEmptyOptionalIfBookDoesntExist(){
        Optional<Book> book = repositoryJpa.findById(124);
        assertThat(book).isEmpty();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @Order(4)
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("не должен добавлять более одной книги")
    @Test
    @Order(5)
    void shouldNotSaveMoreThanOneBook() {
        int count = repositoryJpa.findAll().size();
        Book book = new Book(0, "title", dbAuthors.get(0), List.of(dbGenres.get(0)));
        repositoryJpa.save(book);
        assertThat(repositoryJpa.findAll().size()).isEqualTo(count+1);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @Order(6)
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(repositoryJpa.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("не должен изменять другие книги")
    @Test
    @Order(7)
    void updateBookShouldNotAffectOtherBooks(){
        List<Book> allBooksBefore = repositoryJpa.findAll();
        var bookToUpdate = repositoryJpa.findById(1L).get();
        allBooksBefore.remove(bookToUpdate);
        bookToUpdate.setTitle("newTitle");
        var updatedBook = repositoryJpa.save(bookToUpdate);
        List<Book> allBooksAfter = repositoryJpa.findAll();
        allBooksAfter.remove(updatedBook);
        assertThat(allBooksAfter).containsExactlyElementsOf(allBooksBefore);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @Order(8)
    void shouldDeleteBook() {
        assertThat(repositoryJpa.findById(3L)).isPresent();
        repositoryJpa.deleteById(3L);
        assertThat(repositoryJpa.findById(3L)).isEmpty();
    }

    @DisplayName("не должен удалять другие книги")
    @Test
    @Order(9)
    void shouldNotDeleteOtherBooks() {
        int initialCount = repositoryJpa.findAll().size();
        repositoryJpa.deleteById(2L);
        assertThat(repositoryJpa.findAll().size()).isEqualTo(initialCount-1);
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