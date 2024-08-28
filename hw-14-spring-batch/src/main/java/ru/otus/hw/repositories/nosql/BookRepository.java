package ru.otus.hw.repositories.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.nosql.Book;


public interface BookRepository extends MongoRepository<Book, String> {

}
