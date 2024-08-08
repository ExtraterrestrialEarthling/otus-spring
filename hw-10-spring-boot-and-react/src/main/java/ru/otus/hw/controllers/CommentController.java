package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.request.CommentDtoRq;
import ru.otus.hw.dto.response.CommentDtoRs;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDtoRs> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<CommentDtoRs>> getCommentsByBookId(@RequestParam Long bookId) {
        return ResponseEntity.ok(commentService.findByBookId(bookId));
    }

    @PostMapping()
    public ResponseEntity<CommentDtoRs> createComment(@Valid @RequestBody CommentDtoRq commentDtoRq) {
        return ResponseEntity.status(201).body(commentService.insert(commentDtoRq));
    }
}
