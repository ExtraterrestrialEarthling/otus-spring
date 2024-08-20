package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Set;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book insert(String title, long authorId, Set<Long> genresIds, boolean adultOnly);

    Book update(long id, String title, long authorId, Set<Long> genresIds, boolean adultOnly);

    void deleteById(long id);
}
