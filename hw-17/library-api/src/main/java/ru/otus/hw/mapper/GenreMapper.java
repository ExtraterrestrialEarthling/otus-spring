package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDtoRs entityToDtoRs(Genre genre);
}
