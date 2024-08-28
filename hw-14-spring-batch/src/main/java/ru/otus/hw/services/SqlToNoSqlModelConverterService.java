package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.IdMapping;
import ru.otus.hw.models.nosql.Author;
import ru.otus.hw.models.nosql.Book;
import ru.otus.hw.models.nosql.Comment;
import ru.otus.hw.models.nosql.Genre;
import ru.otus.hw.models.sql.SourceAuthor;
import ru.otus.hw.models.sql.SourceBook;
import ru.otus.hw.models.sql.SourceComment;
import ru.otus.hw.models.sql.SourceGenre;
import ru.otus.hw.repositories.nosql.IdMappingRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SqlToNoSqlModelConverterService {

    private final IdMappingRepository idMappingRepository;

    public Book convertBook(SourceBook sourceBook) {
        String authorId = idMappingRepository
                .findByEntityTypeAndSqlId("author", sourceBook.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("No author mongo id corresponding to id=%d found"
                        .formatted(sourceBook.getAuthor().getId())))
                .getMongoId();
        Author author = new Author(authorId, sourceBook.getAuthor().getFullName());

        Map<Long, String> sqlToMongoGenresMapping = idMappingRepository.findAllByEntityTypeAndSqlIdIn("genre",
                        sourceBook.getGenres().stream()
                                .map(SourceGenre::getId)
                                .toList())
                .stream()
                .collect(Collectors.toMap(IdMapping::getSqlId, IdMapping::getMongoId));

        List<Genre> genres = sourceBook.getGenres().stream()
                .map(genre -> new Genre(sqlToMongoGenresMapping.get(genre.getId()), genre.getName())).toList();

        String newBookId = new ObjectId().toString();
        idMappingRepository.save(new IdMapping("book", sourceBook.getId(), newBookId));
        return new Book(newBookId, sourceBook.getTitle(), author, genres);
    }

    public Author convertAuthor(SourceAuthor sourceAuthor) {
        String newAuthorId = new ObjectId().toString();
        idMappingRepository.save(new IdMapping("author", sourceAuthor.getId(), newAuthorId));
        return new Author(newAuthorId, sourceAuthor.getFullName());
    }

    public Genre convertGenre(SourceGenre sourceGenre) {
        String newGenreId = new ObjectId().toString();
        idMappingRepository.save(new IdMapping("genre", sourceGenre.getId(), newGenreId));
        return new Genre(newGenreId, sourceGenre.getName());
    }

    public Comment convertComment(SourceComment sourceComment) {
        String bookId = idMappingRepository.findByEntityTypeAndSqlId("book", sourceComment.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book mongo id corresponding to id=%d not found"
                        .formatted(sourceComment.getBook().getId()))).getMongoId();
        String newCommentId = new ObjectId().toString();

        return new Comment(newCommentId, sourceComment.getText(), new Book(bookId));
    }
}
