package ru.otus.hw.repositories.nosql;

import ru.otus.hw.models.nosql.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByBookId(String bookId);
}
