package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.request.StudentDtoRq;
import ru.otus.hw.dto.response.StudentDtoRs;
import ru.otus.hw.mapper.StudentMapper;
import ru.otus.hw.models.Student;
import ru.otus.hw.repositories.SkillRepository;
import ru.otus.hw.repositories.StudentRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    private final SkillRepository skillRepository;

    private final TeacherRepository teacherRepository;

    private final StudentMapper studentMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentDtoRs>> getStudentById(@PathVariable String id) {
        return studentRepository.findById(id)
                .map(studentMapper::entityToDtoRs)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<List<StudentDtoRs>>> getAllStudents() {
        return studentRepository.findAll()
                .doOnNext(System.out::println)
                .map(studentMapper::entityToDtoRs)
                .collectList()
                .map(students -> ResponseEntity.ok().body(students));
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDtoRs>> createStudent(@Valid @RequestBody StudentDtoRq studentDtoRq) {
        return teacherRepository.findById(studentDtoRq.getTeacherId())
                .flatMap(teacher -> skillRepository.findByIdIn(studentDtoRq.getSkillIds())
                        .collectList()
                        .flatMap(skills ->
                                studentRepository.save(new Student(null, studentDtoRq.getName(), teacher, skills))))
                .map(savedStudent -> ResponseEntity.status(201).body(studentMapper.entityToDtoRs(savedStudent)))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PutMapping
    public Mono<ResponseEntity<StudentDtoRs>> updateStudent(@Valid @RequestBody StudentDtoRq studentDtoRq) {
        return teacherRepository.findById(studentDtoRq.getTeacherId())
                .flatMap(teacher -> skillRepository.findByIdIn(studentDtoRq.getSkillIds())
                        .collectList()
                        .flatMap(skills ->
                                studentRepository.save(
                                        new Student(studentDtoRq.getId(), studentDtoRq.getName(), teacher, skills))))
                .map(savedStudent -> ResponseEntity.status(201).body(studentMapper.entityToDtoRs(savedStudent)))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteStudentById(@PathVariable String id) {
        return studentRepository.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted student with id %s".formatted(id))));
    }
}
