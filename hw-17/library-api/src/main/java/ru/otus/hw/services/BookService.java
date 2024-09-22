package ru.otus.hw.services;

import ru.otus.hw.dto.request.BookDtoRq;
import ru.otus.hw.dto.response.BookDtoRs;

import java.util.List;

public interface BookService {
    BookDtoRs findById(long id);

    List<BookDtoRs> findAll();

    BookDtoRs insert(BookDtoRq bookDto);

    BookDtoRs update(BookDtoRq bookDto);

    void deleteById(long id);

}
