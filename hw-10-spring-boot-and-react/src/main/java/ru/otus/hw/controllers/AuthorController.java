package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.request.AuthorDtoRq;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDtoRs> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDtoRs>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @PostMapping()
    public ResponseEntity<AuthorDtoRs> createAuthor(@Valid @RequestBody AuthorDtoRq authorDtoRq) {
        return ResponseEntity.status(201).body(authorService.insert(authorDtoRq));
    }
}
