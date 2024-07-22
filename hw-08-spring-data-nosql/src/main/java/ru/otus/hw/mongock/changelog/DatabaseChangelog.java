package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "iv.haidarov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "iv.haidarov", runAlways = true)
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        var author1 = new Document().append("fullName", "Patrick Rothfuss");
        var author2 = new Document().append("fullName", "Arthur C. Clarke");
        var author3 = new Document().append("fullName", "J.K. Rowling");
        authorCollection.insertMany(List.of(author1, author2, author3));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "iv.haidarov", runAlways = true)
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genreCollection = db.getCollection("genres");
        var genre1 = new Document().append("name", "fantasy");
        var genre2 = new Document().append("name", "science fiction");
        genreCollection.insertMany(List.of(genre1, genre2));
    }


    @ChangeSet(order = "004", id = "insertBooks", author = "iv.haidarov", runAlways = true)
    public void insertBooks(MongoDatabase db) {
        List<Document> authors = db.getCollection("authors").find().into(new ArrayList<>());
        List<Document> genres = db.getCollection("genres").find().into(new ArrayList<>());
        MongoCollection<Document> bookCollection = db.getCollection("books");
        var book1 = new Document().append("title", "Harry Potter")
                .append("author", authors.get(2))
                .append("genres", List.of(genres.get(0)));
        var book2 = new Document().append("title", "Kingkiller Chronicle")
                .append("author", authors.get(0))
                .append("genres", List.of(genres.get(0)));
        var book3 = new Document().append("title", "A Space Odyssey")
                .append("author", authors.get(1))
                .append("genres", List.of(genres.get(1)));
        bookCollection.insertMany(List.of(book1, book2, book3));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "iv.haidarov", runAlways = true)
    public void insertComments(MongoDatabase db) {
        List<Document> books = db.getCollection("books").find().into(new ArrayList<>());
        MongoCollection<Document> commentCollection = db.getCollection("comments");

        if (books.size() < 3) {
            throw new IllegalStateException("There are not enough books in the collection to create comments.");
        }

        var comment1 = new Document()
                .append("text", "nice")
                .append("book", new Document("$ref", "books").append("$id", books.get(0).get("_id")));

        var comment2 = new Document()
                .append("text", "good")
                .append("book", new Document("$ref", "books").append("$id", books.get(1).get("_id")));

        var comment3 = new Document()
                .append("text", "amazing")
                .append("book", new Document("$ref", "books").append("$id", books.get(2).get("_id")));

        commentCollection.insertMany(List.of(comment1, comment2, comment3));
    }
}
