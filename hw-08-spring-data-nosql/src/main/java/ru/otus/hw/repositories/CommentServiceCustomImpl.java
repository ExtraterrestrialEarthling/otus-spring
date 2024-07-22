package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentServiceCustomImpl implements CommentRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Override
    public List<Comment> findByBookId(String bookId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("book.$id").is(bookId));
        return mongoOperations.find(query, Comment.class);
    }
}
