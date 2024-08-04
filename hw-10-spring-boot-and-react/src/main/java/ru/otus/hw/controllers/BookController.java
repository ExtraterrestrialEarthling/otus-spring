package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.request.BookDtoRq;
import ru.otus.hw.dto.response.BookDtoRs;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDtoRs> getBookById(@PathVariable Long id) {
        BookDtoRs book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookDtoRs>> getAllBooks() {
        List<BookDtoRs> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDtoRs> createBook(@Valid @RequestBody BookDtoRq bookDtoRq) {
        BookDtoRs createdBook = bookService.insert(bookDtoRq);
        return ResponseEntity.status(201).body(createdBook);
    }

    @PutMapping
    public ResponseEntity<BookDtoRs> updateBook(@Valid @RequestBody BookDtoRq bookDtoRq) {
        BookDtoRs updatedBook = bookService.update(bookDtoRq);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().body("Book deletion successful");
    }
}
