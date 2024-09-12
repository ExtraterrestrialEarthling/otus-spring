package ru.otus.hw.services;

import ru.otus.hw.dto.request.AuthorDtoRq;
import ru.otus.hw.dto.response.AuthorDtoRs;

import java.util.List;

public interface AuthorService {

    List<AuthorDtoRs> findAll();

    AuthorDtoRs findById(long id);

    AuthorDtoRs update(AuthorDtoRq authorDtoRq);

    AuthorDtoRs insert(AuthorDtoRq authorDtoRq);

}
