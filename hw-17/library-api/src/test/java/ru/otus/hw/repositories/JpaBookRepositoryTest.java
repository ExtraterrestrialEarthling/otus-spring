package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.TestDataProvider;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
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
        dbAuthors = TestDataProvider.getDbAuthors();
        dbGenres = TestDataProvider.getDbGenres();
        dbBooks = TestDataProvider.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
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
    void shouldReturnCorrectBookById() {
        var actualBook = repositoryJpa.findById(1L);
        var expectedBook = em.find(Book.class, 1L);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("должен возращать пустой результат, если книга не найдена")
    void shouldReturnEmptyOptionalIfBookDoesntExist(){
        Optional<Book> book = repositoryJpa.findById(124);
        assertThat(book).isEmpty();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0),
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
    @DirtiesContext
    void shouldNotSaveMoreThanOneBook() {
        int count = repositoryJpa.findAll().size();
        Book book = new Book(null, "title", dbAuthors.get(0), List.of(dbGenres.get(0)));
        repositoryJpa.save(book);
        assertThat(repositoryJpa.findAll().size()).isEqualTo(count+1);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @DirtiesContext
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
    @DirtiesContext
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
    @DirtiesContext
    void shouldDeleteBook() {
        assertThat(repositoryJpa.findById(3L)).isPresent();
        repositoryJpa.deleteById(3L);
        assertThat(repositoryJpa.findById(3L)).isEmpty();
    }

    @DisplayName("не должен удалять другие книги")
    @Test
    @DirtiesContext
    void shouldNotDeleteOtherBooks() {
        int initialCount = repositoryJpa.findAll().size();
        repositoryJpa.deleteById(2L);
        assertThat(repositoryJpa.findAll().size()).isEqualTo(initialCount-1);
    }
}