package ru.otus.hw.services;

import ru.otus.hw.dto.request.CommentDtoRq;
import ru.otus.hw.dto.response.CommentDtoRs;

import java.util.List;

public interface CommentService {

    CommentDtoRs findById(long id);

    List<CommentDtoRs> findByBookId(long bookId);

    CommentDtoRs update(CommentDtoRq commentDtoRq);

    CommentDtoRs insert(CommentDtoRq commentDtoRq);

    void deleteById(long id);
}
