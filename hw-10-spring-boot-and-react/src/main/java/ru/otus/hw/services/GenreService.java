package ru.otus.hw.services;

import ru.otus.hw.dto.request.GenreDtoRq;
import ru.otus.hw.dto.response.GenreDtoRs;

import java.util.List;

public interface GenreService {

    List<GenreDtoRs> findAll();

    GenreDtoRs findById(long id);

    GenreDtoRs insert(GenreDtoRq genreDtoRq);
}
