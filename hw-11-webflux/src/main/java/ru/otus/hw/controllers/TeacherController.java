package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.request.TeacherDtoRq;
import ru.otus.hw.dto.response.TeacherDtoRs;
import ru.otus.hw.mapper.TeacherMapper;
import ru.otus.hw.models.Teacher;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TeacherDtoRs>> getTeacherById(@PathVariable String id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::entityToDtoRs)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<List<TeacherDtoRs>>> getAllTeachers() {
        return teacherRepository.findAll()
                .map(teacherMapper::entityToDtoRs)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping()
    public Mono<ResponseEntity<TeacherDtoRs>> createTeacher(@Valid @RequestBody TeacherDtoRq teacherDtoRq) {
        return teacherRepository.save(new Teacher(null, teacherDtoRq.getFullName()))
                .map(savedTeacher -> ResponseEntity.status(201).body(teacherMapper.entityToDtoRs(savedTeacher)))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }
}
