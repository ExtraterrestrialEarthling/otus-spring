package ru.otus.hw.services;

import ru.otus.hw.models.nosql.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
