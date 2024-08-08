package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDtoRs entityToDtoRs(Author author);
}
