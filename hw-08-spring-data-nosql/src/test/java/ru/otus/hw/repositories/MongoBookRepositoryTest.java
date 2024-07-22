package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class MongoBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor(new Author(null, "Doe"));
        book.setGenres(Arrays.asList(new Genre(null, "Fiction"), new Genre(null, "Adventure")));
        book = bookRepository.save(book);
    }

    @Test
    @DisplayName("Должен сохранять новую книгу")
    void shouldSaveBook() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor(new Author("Jane", "Doe"));
        newBook.setGenres(Arrays.asList(new Genre(null, "Science Fiction")));

        Book savedBook = bookRepository.save(newBook);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
    }

    @Test
    @DisplayName("Должен обновлять книгу")
    void shouldUpdateBook() {
        book.setTitle("Old Title");
        book = bookRepository.save(book);

        book.setTitle("Updated Title");
        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getId()).isEqualTo(book.getId());
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName("Должен находить книгу по ID")
    void shouldFindById() {
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Sample Book");
    }

    @Test
    @DisplayName("Должен находить все книги")
    void shouldFindAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotEmpty();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Sample Book");
    }

    @Test
    @DisplayName("Должен удалять книгу по ID")
    void shouldDeleteById() {
        bookRepository.deleteById(book.getId());
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertThat(foundBook).isNotPresent();
    }
}
