package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.request.BookDtoRq;
import ru.otus.hw.dto.response.BookDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDtoRs findById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d is not present".formatted(id)));
        return bookMapper.entityToDtoRs(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDtoRs> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::entityToDtoRs)
                .toList();
    }

    @Override
    @Transactional
    public BookDtoRs insert(BookDtoRq bookDto) {
        Book book = save(bookDto);
        return bookMapper.entityToDtoRs(book);
    }

    @Override
    @Transactional
    public BookDtoRs update(BookDtoRq bookDto) {
        Optional<Book> bookOptional = bookRepository.findById(bookDto.getBookId());
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book for update with id %d is not present"
                    .formatted(bookDto.getBookId()));
        }
        Book book = save(bookDto);
        return bookMapper.entityToDtoRs(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book for deletion with id %d is not present"
                    .formatted(id));
        }
        bookRepository.deleteById(id);
    }

    private Book save(BookDtoRq bookDtoRq) {
        if (isEmpty(bookDtoRq.getGenreIds())) {
            throw new IllegalArgumentException("Genre ids must not be null");
        }
        var author = authorRepository.findById(bookDtoRq.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookDtoRq.getAuthorId())));
        var genreIds = bookDtoRq.getGenreIds();
        var genres = genreRepository.findByIdIn(genreIds);
        if (isEmpty(genres) || genreIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found"
                    .formatted(genreIds));
        }

        var book = new Book(bookDtoRq.getBookId(), bookDtoRq.getTitle(), author, genres);
        return bookRepository.save(book);
    }

}
