package ru.otus.hw.services;

import ru.otus.hw.models.nosql.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String bookId);

    Comment update(String id, String text);

    Comment insert(String text, String bookId);

    void deleteById(String id);
}
