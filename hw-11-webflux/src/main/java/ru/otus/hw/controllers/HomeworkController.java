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
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.request.HomeworkDtoRq;
import ru.otus.hw.dto.response.HomeworkDtoRs;
import ru.otus.hw.mapper.HomeworkMapper;
import ru.otus.hw.models.Homework;
import ru.otus.hw.repositories.HomeworkRepository;
import ru.otus.hw.repositories.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkMapper homeworkMapper;

    private final HomeworkRepository homeworkRepository;

    private final StudentRepository studentRepository;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<HomeworkDtoRs>> getHomeworkById(@PathVariable String id) {
        return homeworkRepository.findById(id)
                .map(homeworkMapper::entityToDtoRs)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @GetMapping()
    public Mono<ResponseEntity<List<HomeworkDtoRs>>> getHomeworksByStudentId(@RequestParam String studentId) {
        return homeworkRepository.findByStudentId(studentId)
                .map(homeworkMapper::entityToDtoRs)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping()
    public Mono<ResponseEntity<HomeworkDtoRs>> createHomework(@Valid @RequestBody Mono<HomeworkDtoRq> homeworkDtoRq) {
        return homeworkDtoRq
                .flatMap(dto -> studentRepository.findById(dto.getStudentId())
                        .flatMap(student ->
                                homeworkRepository.save(
                                        new Homework(null, dto.getText(), student))
                                        .map(savedHomework ->
                                                ResponseEntity.ok(homeworkMapper.entityToDtoRs(savedHomework)))
                                        .switchIfEmpty(Mono.fromCallable(() ->
                                                ResponseEntity.notFound().build()))));
    }
}
