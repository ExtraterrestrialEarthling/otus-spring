package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.request.GenreDtoRq;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.exceptions.EntityAlreadyExistsException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDtoRs> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::entityToDtoRs)
                .toList();
    }

    @Override
    public GenreDtoRs findById(long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d not found".formatted(id)));
        return genreMapper.entityToDtoRs(genre);
    }

    @Transactional
    public GenreDtoRs insert(GenreDtoRq genreDtoRq) {
        if (genreRepository.findByName(genreDtoRq.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("Genre \"%s\" already exists"
                    .formatted(genreDtoRq.getName()));
        }
        Genre genre = genreRepository.save(new Genre(null, genreDtoRq.getName()));
        return genreMapper.entityToDtoRs(genre);
    }
}
