package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.request.AuthorDtoRq;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDtoRs> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::entityToDtoRs).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDtoRs findById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found"
                        .formatted(id)));
        return authorMapper.entityToDtoRs(author);
    }

    @Override
    @Transactional
    public AuthorDtoRs update(AuthorDtoRq authorDtoRq) {
        Optional<Author> author = authorRepository.findById(authorDtoRq.getId());
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author for update with id %d is not present"
                    .formatted(authorDtoRq.getId()));
        }
        Author updatedAuthor = save(authorDtoRq);
        return authorMapper.entityToDtoRs(updatedAuthor);
    }

    @Override
    @Transactional
    public AuthorDtoRs insert(AuthorDtoRq authorDtoRq) {
        return authorMapper.entityToDtoRs(save(authorDtoRq));
    }


    private Author save(AuthorDtoRq authorDtoRq) {
        return authorRepository.save(new Author(authorDtoRq.getId(),
                authorDtoRq.getFullName()));
    }
}
