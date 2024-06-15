package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        String selectById = "SELECT b.id as book_id, b.title as book_title, " +
                "a.id as author_id, a.full_name as author_full_name, " +
                "g.id as genre_id, g.name as genre_name " +
                "FROM books b " +
                "JOIN authors a on b.author_id = a.id " +
                "LEFT JOIN books_genres bg on b.id = bg.book_id " +
                "LEFT JOIN genres g on bg.genre_id = g.id " +
                "WHERE b.id = :id";
        return Optional.ofNullable(jdbc.query(selectById, Map.of("id", id), new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        String deleteById = "DELETE FROM books WHERE id = :id";
        jdbc.update(deleteById, Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        String selectAllBooksWithoutGenres =
                "SELECT b.id as book_id, b.title as book_title, b.author_id as author_id, " +
                        "a.full_name as author_full_name FROM books b " +
                        "JOIN authors a on a.id = b.author_id";
        return jdbc.query(selectAllBooksWithoutGenres, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        String selectAllGenreRelations = "SELECT book_id, genre_id FROM books_genres";
        return jdbc.query(selectAllGenreRelations, new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreIdToGenreMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));

        Map<Long, Book> bookIdToBookMap = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, book -> book));

        relations.forEach(relation -> {
            Book book = bookIdToBookMap.get(relation.bookId());
            Genre genre = genreIdToGenreMap.get(relation.genreId());
            if (book != null && genre != null) {
                book.getGenres().add(genre);
            }
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        String insertBook = "INSERT INTO books (title, author_id) VALUES (:title, :author_id)";
        jdbc.update(insertBook,
                params, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        String updateBook = "UPDATE books SET title = :title, author_id = :authorId WHERE id = :id";
        int updatesCount = jdbc.update(updateBook,
                Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId(), "id", book.getId()));
        if (updatesCount == 0) {
            throw new EntityNotFoundException("Nothing to update");
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<BookGenreRelation> relations = book.getGenres().stream()
                .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
                .toList();
        String updateRelations = "INSERT INTO books_genres (book_id, genre_id) " +
                "VALUES (:bookId, :genreId)";
        jdbc.batchUpdate(updateRelations,
                SqlParameterSourceUtils.createBatch(relations));
    }

    private void removeGenresRelationsFor(Book book) {
        String removeRelations = "DELETE FROM books_genres where book_id = :book_id";
        jdbc.update(removeRelations, Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("book_id");
            String title = rs.getString("book_title");
            Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            List<Genre> genres = new ArrayList<>();
            return new Book(id, title, author, genres);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            List<Genre> genres = new ArrayList<>();

            while (rs.next()) {
                if (book == null) {
                    long id = rs.getLong("book_id");
                    String title = rs.getString("book_title");
                    Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
                    book = new Book(id, title, author, genres);
                }
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int i) throws SQLException {
            long book_id = rs.getLong("book_id");
            long genre_id = rs.getLong("genre_id");
            return new BookGenreRelation(book_id, genre_id);
        }
    }
}


