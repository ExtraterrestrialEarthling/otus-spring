package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Author findById(long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public Author update(long id, String fullName) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author for update with id %d is not present".formatted(id));
        }
        return authorRepository.save(new Author(id, fullName));
    }

    @Override
    @Transactional
    public Author insert(String fullName) {
        return authorRepository.save(new Author(0, fullName));
    }
}
