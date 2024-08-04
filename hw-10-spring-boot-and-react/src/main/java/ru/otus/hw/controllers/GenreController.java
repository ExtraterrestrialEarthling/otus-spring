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
import ru.otus.hw.dto.request.GenreDtoRq;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
@CrossOrigin(origins = "http://localhost:9000")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public ResponseEntity<GenreDtoRs> getGenreById(@PathVariable long id) {
        return ResponseEntity.ok(genreService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<GenreDtoRs>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @PostMapping()
    public ResponseEntity<GenreDtoRs> createGenre(@Valid @RequestBody GenreDtoRq genreDtoRq) {
        return ResponseEntity.status(201).body(genreService.insert(genreDtoRq));
    }
}

