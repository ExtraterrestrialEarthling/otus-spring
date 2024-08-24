package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclService aclService;

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d is not present".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        Book book = save(0, title, authorId, genresIds, adultOnly);
        aclService.setUpAclFor(book);
        return book;
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book for update with id %d is not present".formatted(id));
        }
        Book updatedBook = save(id, title, authorId, genresIds, adultOnly);
        aclService.setUpAclFor(updatedBook);
        return updatedBook;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book for deletion with id %d is not present".formatted(id));
        }
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres, adultOnly);
        return bookRepository.save(book);
    }
}
