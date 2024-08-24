package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author findById(long id);

    Author update(long id, String fullName);

    Author insert(String fullName);
}
