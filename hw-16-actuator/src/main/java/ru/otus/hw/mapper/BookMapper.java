package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.dto.response.BookDtoRs;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDtoRs entityToDtoRs(Book book);

    AuthorDtoRs entityToDtoRs(Author author);

    GenreDtoRs entityToDtoRs(Genre genre);
}