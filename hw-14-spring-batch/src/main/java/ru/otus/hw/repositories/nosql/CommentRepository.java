package ru.otus.hw.repositories.nosql;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.nosql.Comment;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {

}
